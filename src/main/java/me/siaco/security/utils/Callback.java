/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.siaco.security.utils;

/**
 * Created by Siaco
 */
public interface Callback<T> {
    /**
     * Takes something and runs it.
     * @param t
     */
    void run(T t);
}
