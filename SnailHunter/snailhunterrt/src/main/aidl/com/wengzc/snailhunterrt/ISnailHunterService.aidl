// ISnailHunterService.aidl
package com.wengzc.snailhunterrt;

// Declare any non-default types here with import statements

import com.wengzc.snailhunterrt.Snail;

interface ISnailHunterService {

    void catchNewSnail(in Snail snail);
}
