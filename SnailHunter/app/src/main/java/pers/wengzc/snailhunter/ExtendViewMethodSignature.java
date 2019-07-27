package pers.wengzc.snailhunter;

import pers.wengzc.hunterKit.HunterTarget;

public class ExtendViewMethodSignature extends ViewMethodSignature implements TestInterface{

    @Override
    public void fun2 (int i){

    }

    @Override
    public void fun1 (){
        try {
            Thread.sleep(77);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testInterfaceFun() {
        try {
            Thread.sleep(222);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
