package pers.wengzc.snailhunter;

import pers.wengzc.hunterKit.Action;
import pers.wengzc.hunterKit.HunterTarget;

public class ExtendViewMethodSignature extends ViewMethodSignature implements TestInterface{

    @HunterTarget(action = Action.Exclude)
    public void fun2 (int i){

    }

    @HunterTarget(timeConstraint = 666)
    public void fun1 (){

    }

    @HunterTarget(timeConstraint = 888)
    @Override
    public void testInterfaceFun() {

    }
}
