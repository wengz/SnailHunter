package pers.wengzc.snailhunter;


import pers.wengzc.hunterKit.HunterTarget;

public class ExtendViewMethodSignature extends ViewMethodSignature implements TestInterface{

    @Override
    public void fun2 (int i){

    }

    @HunterTarget
    @Override
    public void fun1 (){
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void testInterfaceFun() {

    }
}
