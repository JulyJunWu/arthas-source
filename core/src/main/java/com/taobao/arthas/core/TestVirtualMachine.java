package com.taobao.arthas.core;

import com.sun.tools.attach.VirtualMachine;

import java.util.Enumeration;
import java.util.Properties;

/**
 * 测试 jdk 自带工具
 */
public class TestVirtualMachine {

    public static void main(String[] args) throws Exception{
        VirtualMachine virtualMachine = VirtualMachine.attach("67040");

        Properties systemProperties = virtualMachine.getSystemProperties();
        Enumeration<?> enumeration = systemProperties.propertyNames();
        while (enumeration.hasMoreElements()){
            Object nextElement = enumeration.nextElement();
            Object o = systemProperties.get(nextElement);
            System.out.println("key=" + nextElement + " value=" + o);
        }

    }

}
