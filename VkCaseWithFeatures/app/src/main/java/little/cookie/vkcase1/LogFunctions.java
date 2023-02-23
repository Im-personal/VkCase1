package little.cookie.vkcase1;

public class LogFunctions {
    static void log()
    {
        System.out.println("DEBUG!!");
    }

    static void log(int i)
    {
        System.out.println(i);
    }

    static void log(String str)
    {
        System.out.println(str);
    }

    static void log(boolean bool)
    {
        System.out.println(bool);
    }

    static void log(char c)
    {
        System.out.println(c);
    }

    static void log(float f)
    {
        System.out.println(f);
    }

    static void log(int[] i)
    {
        for (int v : i)
            System.out.println(v);
    }

    static void log(String[] str)
    {
        for (String v : str)
            System.out.println(v);
    }

    static void log(boolean[] bool)
    {
        for (boolean v : bool)
            System.out.println(v);
    }

    static void log(char[] c)
    {
        for (char v : c)
            System.out.println(v);
    }

    static void log(float[] f)
    {
        for (float v : f) System.out.println(v);
    }
}
