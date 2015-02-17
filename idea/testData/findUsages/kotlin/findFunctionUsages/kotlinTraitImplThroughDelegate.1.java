package client;

import server.TraitWithDelegatedWithImpl;

public class Test {
    public static void bar(TraitWithDelegatedWithImpl some) {
        some.foo();
    }
}

// REF: (in k.TraitWithImpl).foo()
