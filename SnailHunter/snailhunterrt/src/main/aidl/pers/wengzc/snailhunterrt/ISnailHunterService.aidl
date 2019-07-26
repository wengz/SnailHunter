// ISnailHunterService.aidl
package pers.wengzc.snailhunterrt;

// Declare any non-default types here with import statements

import pers.wengzc.snailhunterrt.Snail;
import pers.wengzc.snailhunterrt.ISnailWatcher;

interface ISnailHunterService {

    void catchNewSnail(in Snail snail);

    List<Snail> getAllSnail();

    void registerNewSnailWatcher(ISnailWatcher newWatcher);

    void unregisterNewSnailWatcher(ISnailWatcher newWatcher);
}
