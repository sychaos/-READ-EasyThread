/*
 * Copyright (C) 2017 Haoge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lzh.easythread;

import java.util.concurrent.Callable;
/**
 * A Callable Wrapper to delegate {@link Callable#call()}
 */
final class CallableWrapper<T> implements Callable<T> {
    private String name;
    private Callback callback;
    private Callable<T> proxy;

    CallableWrapper(String name, Callback callback, Callable<T> proxy) {
        this.name = name;
        this.callback = callback;
        this.proxy = proxy;
    }

    // 方法 submit(Callable) 和方法 submit(Runnable) 比较类似，但是区别则在于它们接收不同的参数类型。
    // Callable 的实例与 Runnable 的实例很类似，但是 Callable 的 call() 方法可以返回壹個结果。方法 Runnable.run() 则不能返回结果。
    @Override
    public T call() throws Exception {
        Tools.resetThread(Thread.currentThread(),name,callback);
        if (callback != null) {
            callback.onStart(Thread.currentThread());
        }

        // avoid NullPointException
        T t = proxy == null ? null : proxy.call();
        if (callback != null)  {
            callback.onCompleted(Thread.currentThread());
        }
        return t;
    }
}
