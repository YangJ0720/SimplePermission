package yangj.simplepermission.library

/**
 * @author YangJ
 */
interface PermissionListener {

    /**
     * 已授权
     */
    fun onGranted()

    /**
     * 未授权
     *
     * @param permissions
     */
    fun onDenied(permissions: List<String>)
}