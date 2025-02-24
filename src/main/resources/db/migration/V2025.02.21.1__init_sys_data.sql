insert into remind_you.sys_resource (id, name, url, type, method, description, status, parent_id, tenant_id, create_id, create_name, create_time, update_id, update_name, update_time, delete_flag)
values  (1892389701313261569, '系统管理', '/remind/sys', 'route', '', '顶级菜单', true, 0, 1234, 6230574285, 'QQQtx', '2025-02-20 09:43:58', null, null, null, false),
        (1892389874068254721, '角色管理', '/role', 'route', '', '二级菜单', true, 1892389701313261569, 1234, 6230574285, 'QQQtx', '2025-02-20 09:44:40', null, null, null, false),
        (1892389938815725570, '用户管理', '/user', 'route', '', '二级菜单', true, 1892389701313261569, 1234, 6230574285, 'QQQtx', '2025-02-20 09:44:55', null, null, null, false),
        (1892390055002140673, '资源管理', '/resource', 'route', '', '二级菜单', true, 1892389701313261569, 1234, 6230574285, 'QQQtx', '2025-02-20 09:45:23', 6230574285, 'QQQtx', '2025-02-21 13:04:12', false),
        (1892801691147976706, '分页查询系统角色', '/remind/sysRole/pageSysRole', 'api', '', '三级api', true, 1892389874068254721, 1234, 6230574285, 'QQQtx', '2025-02-21 13:01:04', null, null, null, false),
        (1892801752833605634, '新增或修改系统角色', '/remind/sysRole/saveOrUpdateSysRole', 'api', '', '三级api', true, 1892389874068254721, 1234, 6230574285, 'QQQtx', '2025-02-21 13:01:19', null, null, null, false),
        (1892801787830878210, '根据id查询系统角色', '/remind/sysRole/getSysRoleById', 'api', '', '三级api', true, 1892389874068254721, 1234, 6230574285, 'QQQtx', '2025-02-21 13:01:27', null, null, null, false),
        (1892801827878092801, '更新角色状态', '/remind/sysRole/updateStatus', 'api', '', '三级api', true, 1892389874068254721, 1234, 6230574285, 'QQQtx', '2025-02-21 13:01:37', null, null, null, false),
        (1892801865782018049, '删除系统角色', '/remind/sysRole/removeByRoleId', 'api', '', '三级api', true, 1892389874068254721, 1234, 6230574285, 'QQQtx', '2025-02-21 13:01:46', null, null, null, false),
        (1892802144250249218, '分页查询', '/remind/sysUser/pageSysUser', 'api', '', '三级api', true, 1892389938815725570, 1234, 6230574285, 'QQQtx', '2025-02-21 13:02:52', null, null, null, false),
        (1892802180757471234, '保存或编辑用户', '/remind/sysUser/saveOrUpdateSysUser', 'api', '', '三级api', true, 1892389938815725570, 1234, 6230574285, 'QQQtx', '2025-02-21 13:03:01', null, null, null, false),
        (1892802215100432386, '根据id查询用户', '/remind/sysUser/getSysUserById', 'api', '', '三级api', true, 1892389938815725570, 1234, 6230574285, 'QQQtx', '2025-02-21 13:03:09', null, null, null, false),
        (1892802250760404994, '删除用户', '/remind/sysUser/removeByUserId', 'api', '', '三级api', true, 1892389938815725570, 1234, 6230574285, 'QQQtx', '2025-02-21 13:03:18', null, null, null, false),
        (1892803979652542466, '分页查询', '/remind/sysResource/pageResource', 'api', '', '三级api', true, 1892390055002140673, 1234, 6230574285, 'QQQtx', '2025-02-21 13:10:10', null, null, null, false),
        (1892804443450290178, '根据pid查询资源', '/remind/sysResource/listResourceByParentId', 'api', '', '三级api', true, 1892390055002140673, 1234, 6230574285, 'QQQtx', '2025-02-21 13:12:01', null, null, null, false),
        (1892804496990580738, '获取资源树', '/remind/sysResource/treeResource', 'api', '', '三级api', true, 1892390055002140673, 1234, 6230574285, 'QQQtx', '2025-02-21 13:12:13', null, null, null, false),
        (1892804532621193217, '根据id查询资源', '/remind/sysResource/getResourceById', 'api', '', '三级api', true, 1892390055002140673, 1234, 6230574285, 'QQQtx', '2025-02-21 13:12:22', null, null, null, false),
        (1892804567148703745, '保存或编辑资源', '/remind/sysResource/saveOrUpdateResource', 'api', '', '三级api', true, 1892390055002140673, 1234, 6230574285, 'QQQtx', '2025-02-21 13:12:30', null, null, null, false),
        (1892804603811115009, '更新资源状态', '/remind/sysResource/updateStatus', 'api', '', '三级api', true, 1892390055002140673, 1234, 6230574285, 'QQQtx', '2025-02-21 13:12:39', null, null, null, false),
        (1892804648929243138, '删除资源', '/remind/sysResource/removeResource', 'api', '', '三级api', true, 1892390055002140673, 1234, 6230574285, 'QQQtx', '2025-02-21 13:12:50', null, null, null, false),
        (1892808128150597633, '保存角色资源关联表', '/remind/sysRoleResource/saveRoleResource', 'api', '', '三级api', true, 1892389701313261569, 1234, 6230574285, 'QQQtx', '2025-02-21 13:26:39', null, null, null, false),
        (1892808164263555073, '根据角色id查询角色资源关联表', '/remind/sysRoleResource/listRoleResourceByRoleId', 'api', '', '三级api', true, 1892389701313261569, 1234, 6230574285, 'QQQtx', '2025-02-21 13:26:48', null, null, null, false);

insert into remind_you.sys_user (id, name, account, password, telephone, status, sys_role_id, user_type, tenant_id, create_id, create_name, create_time, update_id, update_name, update_time, delete_flag)
values  (1890408733019316226, 'QQQtx', 6230574285, '$2a$10$wdj5sy/qZmaNJISabM5wmuiiGjEkpGT2.fUBNzd5OOvjaxVkRb88y', '18246386528', true, 1892386208879919105, 'sys', 1234, 1, 'admin', '2025-02-14 22:32:19', 6230574285, 'QQQtx', '2025-02-21 13:53:16', false);

insert into remind_you.sys_role (id, name, status, remark, tenant_id, create_id, create_name, create_time, update_id, update_name, update_time, delete_flag)
values  (1892386208879919105, 'admin', true, '超级管理员', 1234, 6230574285, 'QQQtx', '2025-02-20 09:30:06', 6230574285, 'QQQtx', '2025-02-21 12:59:30', false);

insert into remind_you.sys_role_resource (id, sys_role_id, sys_resource, tenant_id, create_id, create_name, create_time, update_id, update_name, update_time, delete_flag)
values  (1892810236325158914, 1892386208879919105, 1892801691147976706, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236325158915, 1892386208879919105, 1892801752833605634, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236325158916, 1892386208879919105, 1892801787830878210, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236325158917, 1892386208879919105, 1892801827878092801, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236325158918, 1892386208879919105, 1892801865782018049, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236325158919, 1892386208879919105, 1892802144250249218, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236325158920, 1892386208879919105, 1892802180757471234, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236325158921, 1892386208879919105, 1892802215100432386, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236325158922, 1892386208879919105, 1892802250760404994, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684865, 1892386208879919105, 1892803979652542466, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684866, 1892386208879919105, 1892804443450290178, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684867, 1892386208879919105, 1892804496990580738, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684868, 1892386208879919105, 1892804532621193217, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684869, 1892386208879919105, 1892804567148703745, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684870, 1892386208879919105, 1892804603811115009, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684871, 1892386208879919105, 1892804648929243138, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684872, 1892386208879919105, 1892808128150597633, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false),
        (1892810236379684873, 1892386208879919105, 1892808164263555073, 1234, 6230574285, 'QQQtx', '2025-02-21 13:35:02', null, null, null, false);