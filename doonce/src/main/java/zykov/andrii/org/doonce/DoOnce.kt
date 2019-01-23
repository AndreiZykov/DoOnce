package zykov.andrii.org.doonce

class DoOnce {
    companion object {
        fun get(): IDoOnce {
            return DoOnceImpl.get()
        }
    }
}