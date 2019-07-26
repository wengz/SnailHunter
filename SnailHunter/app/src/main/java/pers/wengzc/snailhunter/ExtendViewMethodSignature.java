package pers.wengzc.snailhunter;

import pers.wengzc.hunterKit.HunterTarget;

public class ExtendViewMethodSignature extends ViewMethodSignature implements TestInterface{

    @Override
    public void fun2 (int i){

    }

    @HunterTarget
    @Override
    public void fun1 (){
        try {
            Thread.sleep(123);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @HunterTarget(timeConstraint = 100)
    @Override
    public void testInterfaceFun() {
        try {
            Thread.sleep(234);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
