#include <stdlib.h>

#include "CPythonAdapter.h"
#include "utils.h"

#include "symbolicadapter.h"


static PyObject *
symbolic_handler(Py_ssize_t n, PyObject *const *args, void *param) {
    JavaEnvironment *env = (JavaEnvironment *) param;
    printf("IN SYMBOLIC HANDLER:");
    for (int i = 0; i < n; i++) {
        printf(" ");
        PyObject_Print(args[i], stdout, 0);
    }
    printf("\n");
    fflush(stdout);

    jstring arg = (*(env->env))->NewStringUTF(env->env, "");
    jobject java_result = (*(env->env))->CallStaticObjectMethod(env->env, env->cpython_adapter_cls, env->handler_mid, arg);
    if (PyUnicode_CompareWithASCIIString(args[0], "LOAD_CONST") == 0 || PyUnicode_CompareWithASCIIString(args[0], "BUILD_LIST") == 0)
        return Py_None;

    return wrap_java_object(env, java_result);
}

JNIEXPORT void JNICALL
Java_CPythonAdapter_run(JNIEnv *env, jobject cpython_adapter, jstring code, jstring func_name, jobjectArray symbolic_args) {
    Py_Initialize();
    JavaEnvironment j_env;

    jboolean is_copy1, is_copy2;
    const char *c_code = (*env)->GetStringUTFChars(env, code, &is_copy1);
    const char *c_func_name = (*env)->GetStringUTFChars(env, func_name, &is_copy2);

    j_env.cpython_adapter_cls = (*env)->GetObjectClass(env, cpython_adapter);
    j_env.handler_mid = (*env)->GetStaticMethodID(env, j_env.cpython_adapter_cls, "handler", "(Ljava/lang/String;)LSymbol;");
    j_env.env = env;

    SymbolicAdapter *adapter = create_new_adapter(symbolic_handler, &j_env);

    char *cmd = malloc(strlen(c_func_name) + 10);
    sprintf(cmd, "eval(\"%s\")", c_func_name);
    PyObject *function = run_python(c_code, cmd);
    Py_ssize_t n = (*env)->GetArrayLength(env, symbolic_args);
    PyObject **args = malloc(sizeof(PyObject *) * n);
    for (int i = 0; i < n; i++) {
        args[i] = PyTuple_New(2);
        PyTuple_SetItem(args[i], 0, Py_None);  // concrete value
        jobject symbolic = (*env)->GetObjectArrayElement(env, symbolic_args, i);
        PyTuple_SetItem(args[i], 1, wrap_java_object(&j_env, symbolic));
    }

    PyObject *result = SymbolicAdapter_run((PyObject *) adapter, function, n, args);
    printf("RESULT: ");
    PyObject_Print(result, stdout, 0);
    printf("\n");

    free(cmd);
    free(args);
    Py_FinalizeEx();  // free Python
}
