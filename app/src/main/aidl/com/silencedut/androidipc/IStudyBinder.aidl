// ICalcAIDL.aidl
package com.silencedut.androidipc;

// Declare any non-default types here with import statements

interface IStudyBinder {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    String studyBinder(String name);
}
