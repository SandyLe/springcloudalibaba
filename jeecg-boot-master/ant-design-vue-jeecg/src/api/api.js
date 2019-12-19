import { getAction,deleteAction,putAction,postAction} from '@/api/manage'

////根路径
// const doMian = "/jeecg-boot/";
////图片预览请求地址
// const imgView = "http://localhost:8080/jeecg-boot/sys/common/view/";

//角色管理
const addRole = (params)=>postAction("/sys/role/add",params);
const editRole = (params)=>putAction("/sys/role/edit",params);
// const getRoleList = (params)=>getAction("/sys/role/list",params);
// const deleteRole = (params)=>deleteAction("/sys/role/delete",params);
// const deleteRoleList = (params)=>deleteAction("/sys/role/deleteBatch",params);
const checkRoleCode = (params)=>getAction("/sys/role/checkRoleCode",params);
const queryall = (params)=>getAction("/sys/role/queryall",params);

//用户管理
const addUser = (params)=>postAction("/sys/user/add",params);
const editUser = (params)=>putAction("/sys/user/edit",params);
const queryUserRole = (params)=>getAction("/sys/user/queryUserRole",params);
const getUserList = (params)=>getAction("/sys/user/list",params);
// const deleteUser = (params)=>deleteAction("/sys/user/delete",params);
// const deleteUserList = (params)=>deleteAction("/sys/user/deleteBatch",params);
const frozenBatch = (params)=>putAction("/sys/user/frozenBatch",params);
//验证用户是否存在
const checkOnlyUser = (params)=>getAction("/sys/user/checkOnlyUser",params);
//改变密码
const changPassword = (params)=>putAction("/sys/user/changPassword",params);

//权限管理
const addPermission= (params)=>postAction("/sys/permission/add",params);
const editPermission= (params)=>putAction("/sys/permission/edit",params);
const getPermissionList = (params)=>getAction("/sys/permission/list",params);
/*update_begin author:wuxianquan date:20190908 for:添加查询一级菜单和子菜单查询api */
const getSystemMenuList = (params)=>getAction("/sys/permission/getSystemMenuList",params);
const getSystemSubmenu = (params)=>getAction("/sys/permission/getSystemSubmenu",params);
/*update_end author:wuxianquan date:20190908 for:添加查询一级菜单和子菜单查询api */

// const deletePermission = (params)=>deleteAction("/sys/permission/delete",params);
// const deletePermissionList = (params)=>deleteAction("/sys/permission/deleteBatch",params);
const queryTreeList = (params)=>getAction("/sys/permission/queryTreeList",params);
const queryTreeListForRole = (params)=>getAction("/sys/role/queryTreeList",params);
const queryListAsync = (params)=>getAction("/sys/permission/queryListAsync",params);
const queryRolePermission = (params)=>getAction("/sys/permission/queryRolePermission",params);
const saveRolePermission = (params)=>postAction("/sys/permission/saveRolePermission",params);
//const queryPermissionsByUser = (params)=>getAction("/sys/permission/queryByUser",params);
const queryPermissionsByUser = (params)=>getAction("/sys/permission/getUserPermissionByToken",params);
const loadAllRoleIds = (params)=>getAction("/sys/permission/loadAllRoleIds",params);
const getPermissionRuleList = (params)=>getAction("/sys/permission/getPermRuleListByPermId",params);
const queryPermissionRule = (params)=>getAction("/sys/permission/queryPermissionRule",params);

// 部门管理
const queryDepartTreeList = (params)=>getAction("/sys/sysDepart/queryTreeList",params);
const queryIdTree = (params)=>getAction("/sys/sysDepart/queryIdTree",params);
const queryParentName   = (params)=>getAction("/sys/sysDepart/queryParentName",params);
const searchByKeywords   = (params)=>getAction("/sys/sysDepart/searchBy",params);
const deleteByDepartId   = (params)=>deleteAction("/sys/sysDepart/delete",params);

//日志管理
//const getLogList = (params)=>getAction("/sys/log/list",params);
const deleteLog = (params)=>deleteAction("/sys/log/delete",params);
const deleteLogList = (params)=>deleteAction("/sys/log/deleteBatch",params);

//数据字典
const addDict = (params)=>postAction("/sys/dict/add",params);
const editDict = (params)=>putAction("/sys/dict/edit",params);
//const getDictList = (params)=>getAction("/sys/dict/list",params);
const treeList = (params)=>getAction("/sys/dict/treeList",params);
// const delDict = (params)=>deleteAction("/sys/dict/delete",params);
//const getDictItemList = (params)=>getAction("/sys/dictItem/list",params);
const addDictItem = (params)=>postAction("/sys/dictItem/add",params);
const editDictItem = (params)=>putAction("/sys/dictItem/edit",params);
//const delDictItem = (params)=>deleteAction("/sys/dictItem/delete",params);
//const delDictItemList = (params)=>deleteAction("/sys/dictItem/deleteBatch",params);

//字典标签专用（通过code获取字典数组）
export const ajaxGetDictItems = (code, params)=>getAction(`/sys/dict/getDictItems/${code}`,params);

//系统通告
const doReleaseData = (params)=>getAction("/sys/annountCement/doReleaseData",params);
const doReovkeData = (params)=>getAction("/sys/annountCement/doReovkeData",params);
//获取系统访问量
const getLoginfo = (params)=>getAction("/sys/loginfo",params);
const getVisitInfo = (params)=>getAction("/sys/visitInfo",params);
//数据日志访问
// const getDataLogList = (params)=>getAction("/sys/dataLog/list",params);

// 根据部门主键查询用户信息
const queryUserByDepId = (params)=>getAction("/sys/user/queryUserByDepId",params);

// 查询用户角色表里的所有信息
const queryUserRoleMap = (params)=>getAction("/sys/user/queryUserRoleMap",params);
// 重复校验
const duplicateCheck = (params)=>getAction("/sys/duplicate/check",params);
// 加载分类字典
const loadCategoryData = (params)=>getAction("/sys/category/loadAllData",params);

// 销售订单
const addSaleOrder = (params)=>postAction("/saleOrder/add",params);
const addCustomerType = (params)=>postAction("/customerType/add",params)
const editCustomerType = (params)=>postAction("/customerType/edit",params)
const getCustomerTypeList = (params)=>getAction("/customerType/getList",params)
const addCustomerSource = (params)=>postAction("/customerSource/add",params)
const editCustomerSource = (params)=>postAction("/customerSource/edit",params)
const getCustomerSourceList = (params)=>getAction("/customerSource/getList",params)
const addMaterialBrand = (params)=>postAction("/materialBrand/add",params)
const editMaterialBrand = (params)=>postAction("/materialBrand/edit",params)
const getMaterialBrandList = (params)=>getAction("/materialBrand/getList",params)
const addMaterialType = (params)=>postAction("/materialType/add",params)
const editMaterialType = (params)=>postAction("/materialType/edit",params)
const getMaterialTypeList = (params)=>getAction("/materialType/getList",params)
const addMaterialUnit = (params)=>postAction("/materialUnit/add",params)
const editMaterialUnit = (params)=>postAction("/materialUnit/edit",params)
const getMaterialUnitList = (params)=>getAction("/materialUnit/getList",params)
const addWarehouse = (params)=>postAction("/warehouse/add",params)
const editWarehouse = (params)=>postAction("/warehouse/edit",params)
const getAreaList = (params)=>getAction("/area/getList",params)
const getCustomerOne = (params)=>getAction("/customer/getOne",params)
const saveCustomer = (params)=>postAction("/customer/save",params)
const getCustomerList =  (params)=>getAction("/customer/getList",params)
const getDeliveryInfo = (params)=>getAction("/customer/getDeliveryInfo",params)
const addVendor = (params)=>postAction("/vendor/add",params)
const editVendor = (params)=>postAction("/vendor/edit",params)
const addMaterial = (params)=>postAction("/material/add",params)
const editMaterial = (params)=>postAction("/material/edit",params)
const getMaterialOne = (params)=>getAction("/material/getOne",params)
const getMaterialList = (params)=>getAction("/material/getList",params)
const getMaterialSelfUnitList = (params)=>getAction("/materialSelfUnit/getList",params)
const addMaterialSelfUnit = (params)=>postAction("/materialSelfUnit/add",params)
const editMaterialSelfUnit = (params)=>postAction("/materialSelfUnit/edit",params)
const addMaterialPrice = (params)=>postAction("/materialPrice/add",params)
const editMaterialPrice = (params)=>postAction("/materialPrice/edit",params)


const loadShopData = (params)=>getAction("/sys/sysDepart/getSysDepartList",params)
const getAllUser = (params)=>getAction("/sys/user/getAll",params)


export {
  // imgView,
  // doMian,
  addRole,
  editRole,
  checkRoleCode,
  addUser,
  editUser,
  queryUserRole,
  getUserList,
  queryall,
  frozenBatch,
  checkOnlyUser,
  changPassword,
  getPermissionList,
  addPermission,
  editPermission,
  queryTreeList,
  queryListAsync,
  queryRolePermission,
  saveRolePermission,
  queryPermissionsByUser,
  loadAllRoleIds,
  getPermissionRuleList,
  queryPermissionRule,
  queryDepartTreeList,
  queryIdTree,
  queryParentName,
  searchByKeywords,
  deleteByDepartId,
  deleteLog,
  deleteLogList,
  addDict,
  editDict,
  treeList,
  addDictItem,
  editDictItem,
  doReleaseData,
  doReovkeData,
  getLoginfo,
  getVisitInfo,
  queryUserByDepId,
  queryUserRoleMap,
  duplicateCheck,
  queryTreeListForRole,
  getSystemMenuList,
  getSystemSubmenu,
  loadCategoryData,
  addSaleOrder,
  addCustomerType,
  editCustomerType,
  getCustomerTypeList,
  addCustomerSource,
  editCustomerSource,
  getCustomerSourceList,
  addMaterialBrand,
  editMaterialBrand,
  addMaterialType,
  editMaterialType,
  addMaterialUnit,
  editMaterialUnit,
  loadShopData,
  getAllUser,
  addWarehouse,
  editWarehouse,
  getAreaList,
  saveCustomer,
  getCustomerOne,
  getDeliveryInfo,
  addVendor,
  editVendor,
  addMaterial,
  editMaterial,
  getMaterialTypeList,
  getMaterialBrandList,
  getMaterialUnitList,
  getMaterialOne,
  getMaterialSelfUnitList,
  addMaterialSelfUnit,
  editMaterialSelfUnit,
  getMaterialList,
  addMaterialPrice,
  editMaterialPrice,
  getCustomerList
}



