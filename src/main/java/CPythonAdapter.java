public class CPythonAdapter {
    public native void run(String code, String functionName, Symbol[] args_symbolic);
    static {
        //System.load("/home/tochilinak/Documents/projects/utbot/cpython/libpython3.11.so");
        System.loadLibrary("cpythonadapter");
    }

    static Symbol handler(String cmd, Symbol[] args) {
        System.out.print("Hello from Java! Args:");
        for (Symbol arg : args)
            System.out.print(" " + arg.getAsString());
        System.out.println();
        System.out.flush();
        return new Symbol(cmd);
    }
}
