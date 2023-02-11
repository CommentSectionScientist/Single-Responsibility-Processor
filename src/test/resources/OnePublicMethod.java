package de.ossi;

import de.ossi.annotation.SingleResponsibility;

@SingleResponsibility
public class OnePublicMethod {
    public void method1() {}

    private void privateMethod1() {}
}