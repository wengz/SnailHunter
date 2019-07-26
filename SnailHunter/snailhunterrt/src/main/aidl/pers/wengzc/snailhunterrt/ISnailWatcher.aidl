// ISnailWatcher.aidl
package pers.wengzc.snailhunterrt;

// Declare any non-default types here with import statements

import pers.wengzc.snailhunterrt.Snail;

interface ISnailWatcher {

   void onCatchNewSnail(in Snail snail);

}
