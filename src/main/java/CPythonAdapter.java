public class CPythonAdapter {
    public native void run(String code, String functionName, Symbol[] args_symbolic);
    static {
        //System.load("/home/tochilinak/Documents/projects/utbot/cpython/libpython3.11.so");
        System.loadLibrary("cpythonadapter");
    }

    static Symbol handler(String cmd) {
        System.out.println("Hello from Java!");
        System.out.flush();
        return new Symbol(cmd);
    }
}
