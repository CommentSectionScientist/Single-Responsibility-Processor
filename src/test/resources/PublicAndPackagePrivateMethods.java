package de.ossi;

import de.ossi.annotation.SingleResponsibility;

@SingleResponsibility
public class PublicAndPackagePrivateMethods {
    public void method1() {}

    void method2() {}

    private void privateMethod1() {}
}