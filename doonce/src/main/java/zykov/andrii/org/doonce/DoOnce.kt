package zykov.andrii.org.doonce

class DoOnce {
    companion object {
        /**
         * get singleton instance of DoOnce,
         */
        fun get(): IDoOnce {
            return DoOnceImpl.get()
        }
    }
}