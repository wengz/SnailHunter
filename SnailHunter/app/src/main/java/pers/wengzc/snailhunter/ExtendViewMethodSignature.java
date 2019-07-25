package pers.wengzc.snailhunter;

public class ExtendViewMethodSignature extends ViewMethodSignature implements TestInterface{

    @Override
    public void fun2 (int i){

    }

    @Override
    public void fun1 (){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testInterfaceFun() {

    }
}
