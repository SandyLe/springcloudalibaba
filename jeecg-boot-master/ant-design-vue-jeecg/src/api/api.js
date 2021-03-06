import { getAction,deleteAction,putAction,postAction,asynPostAction} from '@/api/manage'

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
const getDepartById = (params)=>getAction("/sys/sysDepart/getOne",params);

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
const addSaleOrder = (params)=>postAction("/saleOrder/add",params)
const editSaleOrder = (params)=>postAction("/saleOrder/edit",params)
const getSaleOrderOne = (params)=>getAction("/saleOrder/getOne",params)
const getSaleOrderByCode = (params)=>getAction("/saleOrder/getOneByCode",params)
const delivery = (params)=>postAction("/saleOrder/delivery",params)
const disableSaleOrder = (params)=>postAction("/saleOrder/disable",params)
const getSaleOrderDeliveryInfo = (params)=>getAction("/saleOrderDeliveryInfo/getList",params)
const getDeliveryInfoBySourceId = (params)=>getAction("/saleOrderDeliveryInfo/getBySourceId",params)
const getDeliveryMtls = (params)=>getAction("/inventoryOut/mtl/getList",params)
const getPutInMtls = (params)=>getAction("/inventoryIn/mtl/getList",params)
const deliveryStockOut = (params)=>postAction("/inventoryOut/mtls/stockout",params)
const putStockIn = (params)=>postAction("/inventoryIn/mtls/stockin",params)
const addSaleMtlOrder = (params)=>postAction("/saleOrderMtl/add",params)
const editSaleMtlOrder = (params)=>postAction("/saleOrderMtl/edit",params)
const getlSaleMtlList = (params)=>getAction("/saleOrderMtl/getList",params)
const getlSaleMtlOneByMtlId = (params)=>getAction("/saleOrderMtl/mtl/getOne",params)
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
const getMaterialUnitListByIds = (params)=>getAction("/materialUnit/getListByIds",params)
const addWarehouse = (params)=>postAction("/warehouse/add",params)
const editWarehouse = (params)=>postAction("/warehouse/edit",params)
const getWarehouseList = (params)=>getAction("/warehouse/getList",params)
const getAreaList = (params)=>getAction("/area/getList",params)
const getAreaOne = (params)=>getAction("/area/getOne",params)
const getCustomerOne = (params)=>getAction("/customer/getOne",params)
const saveCustomer = (params)=>postAction("/customer/save",params)
const getCustomerList =  (params)=>getAction("/customer/getList",params)
const getDeliveryInfo = (params)=>getAction("/customer/getDeliveryInfo",params)
const searchCustomer = (params)=>getAction("/customer/search",params)
const addVendor = (params)=>postAction("/vendor/add",params)
const editVendor = (params)=>postAction("/vendor/edit",params)
const getVendorList = (params)=>getAction("/vendor/getList",params)
const addMaterial = (params)=>postAction("/material/add",params)
const editMaterial = (params)=>postAction("/material/edit",params)
const getMaterialOne = (params)=>getAction("/material/getOne",params)
const getMaterialList = (params)=>getAction("/material/getList",params)
const getMaterialListByIds = (params)=>getAction("/material/getListByIds",params)
const searchMaterial = (params)=>getAction("/material/search",params)
const getMaterialSelfUnitList = (params)=>getAction("/materialSelfUnit/getList",params)
const addMaterialSelfUnit = (params)=>postAction("/materialSelfUnit/add",params)
const editMaterialSelfUnit = (params)=>postAction("/materialSelfUnit/edit",params)
const addMaterialPrice = (params)=>postAction("/materialPrice/add",params)
const editMaterialPrice = (params)=>postAction("/materialPrice/edit",params)
const getMtlPrice = (params)=>getAction("/materialPrice/getMtlPrice",params)
const addExpense = (params)=>postAction("/expense/add",params)
const editExpense = (params)=>postAction("/expense/edit",params)
const getExpenseList = (params)=>getAction("/expense/getList",params)
const addSaleOrderExpense = (params)=>postAction("/saleOrderExpense/add",params)
const editSaleOrderExpense = (params)=>postAction("/saleOrderExpense/edit",params)
const getSaleOrderExpenseList = (params)=>getAction("/saleOrderExpense/getList",params)
const getSaleOrderExpenseOne = (params)=>getAction("/saleOrderExpense/expense/getOne",params)
const addSaleOrderCost = (params)=>postAction("/saleOrderCost/add",params)
const editSaleOrderCost = (params)=>postAction("/saleOrderCost/edit",params)
const getSaleOrderCostList = (params)=>getAction("/saleOrderCost/getList",params)
const getSaleOrderCostOne = (params)=>getAction("/saleOrderCost/expense/getOne",params)
const handleSaleOrderCost = (params)=>getAction("/saleOrderCost/handle",params)
const addStocking = (params)=>postAction("/stocking/add",params)
const editStocking = (params)=>postAction("/stocking/edit",params)
const handleStocking = (params)=>postAction("/stocking/handleStocking",params)
const editBillCodeBuilder = (params)=>postAction("/billCodeBuilder/edit",params)
const addBillCodeBuilder = (params)=>postAction("/billCodeBuilder/add",params)
const viewInventoryLog = (params)=>getAction("/inventoryLog/getList",params)

const getDiscountTypeList = (params)=>getAction("/basic/fc/enum/discountType/getList",params)
const getBillTypeList = (params)=>getAction("/basic/fc/enum/billType/getList",params)
const getDateFmtList = (params)=>getAction("/basic/fc/enum/dateFormat/getList",params)
const getAddressTypeList = (params)=>getAction("/basic/fc/enum/addressType/getList",params)
const getPayModeTypeList = (params)=>getAction("/basic/fc/enum/payMode/getList",params)
const getDeliveryTypeList = (params)=>getAction("/basic/fc/enum/deliveryType/getList",params)
const getWorkTypeList = (params)=>getAction("/basic/fc/enum/workType/getList",params)
const getInvoiceTypeList = (params)=>getAction("/basic/fc/enum/invoiceType/getList",params)
const getInvoiceTextureList = (params)=>getAction("/basic/fc/enum/invoiceTexture/getList",params)
const getReturnTypeList = (params)=>getAction("/basic/fc/enum/returnType/getList",params)
const getPriceSpaceModeList = (params)=>getAction("/basic/fc/enum/priceSpaceMode/getList",params)

const loadShopData = (params)=>getAction("/sys/sysDepart/getSysDepartList",params)
const getAllUser = (params)=>getAction("/sys/user/getAll",params)

// 辅助属性
const addSupplementary = (params)=>postAction("/supplementary/add",params)  // 新增辅助属性
const editSupplementary = (params)=>postAction("/supplementary/edit",params)  // 编辑辅助属性
const getSupplementaryOne = (params)=>getAction("/supplementary/getOne",params)  // 获取辅助属性
const getSupplementaryList = (params)=>getAction("/supplementary/getList",params)  // 获取辅助属性
const addSupplementaryValue = (params)=>postAction("/supplementaryValue/add",params)  // 新增辅助属性值
const editSupplementaryValue = (params)=>postAction("/supplementaryValue/edit",params)  // 编辑辅助属性值
const addMaterialAuxiliary = (params)=>postAction("/materialAuxiliary/add",params)  // 新增产品辅助属性
const editMaterialAuxiliary = (params)=>postAction("/materialAuxiliary/edit",params)  // 编辑产品辅助属性
const addMaterialAuxiliaryItem = (params)=>postAction("/materialAuxiliaryItem/add",params)  // 新增产品辅助属性
const editMaterialAuxiliaryItem = (params)=>postAction("/materialAuxiliaryItem/edit",params)  // 编辑产品辅助属性
const getSupplementaryValListBySourceCode = (params)=>getAction("/supplementaryValue/getListBySourceCode",params)  // 获取辅助属性值
const getMaterialAuxiliaryList = (params)=>getAction("/materialAuxiliary/getList",params)  // 获取产品辅助属性列表
const getMaterialAuxiliaryListBySourceIds = (params)=>getAction("/materialAuxiliary/getListBySourceIds",params)  // 获取产品辅助属性列表

//采购
const purchasequeryById = (params)=>getAction("/purchase/queryById",params)
const purchasedetailDelete = (params)=>deleteAction("/purchaseMtl/delete",params)
const getPurchaseByCode = (params)=>getAction("/purchase/getOneByCode",params)//采购订单
const getPurchaseMtlList = (params)=>getAction("/purchaseMtl/getList",params)//采购订单
const getPurchaseMtlOne = (params)=>getAction("/purchaseMtl/getPurchaseMtlOne",params)//采购订单产品


const inventoryInedit = (params)=>postAction("/inventoryIn/edit",params)  //入库单编辑
const inventoryOutSave = (params)=>postAction("/inventoryOut/save",params)  // 新增出库单
const inventoryInSave = (params)=>postAction("/inventoryIn/save",params)  // 新增入库单

const purchaseReturnQueryById = (params)=>getAction("/purchasereturn/queryById",params)
const purchasereturnadd = (params)=>postAction("/purchasereturn/add",params)  //新增采购退货
const purchasereturnedit = (params)=>putAction("/purchasereturn/edit",params)  //修改采购退货
const purchaseReturnDetailDelete = (params)=>deleteAction("/purchaseReturnMtl/delete",params)
const addLogisticsCompany = (params)=>postAction("/logisticsCompany/add",params)
const editLogisticsCompany = (params)=>postAction("/logisticsCompany/edit",params)
const getLogisticsCompanyList = (params)=>getAction("/logisticsCompany/getList",params)

// 销售退货
const addSaleOrderReturn = (params)=>postAction("/saleOrderReturn/add",params)
const editSaleOrderReturn = (params)=>postAction("/saleOrderReturn/edit",params)
const saleOrderReturncheckIn = (params)=>postAction("/saleOrderReturn/checkIn",params)
const getSaleOrderReturnOne = (params)=>getAction("/saleOrderReturn/getOne",params)
const addSaleMtlOrderReturn = (params)=>postAction("/saleOrderReturnMtl/add",params)
const editSaleMtlOrderReturn = (params)=>postAction("/saleOrderReturnMtl/edit",params)
const getSaleOrderReturnMtlList = (params)=>getAction("/saleOrderReturnMtl/getList",params)
const addSaleOrderReturnExpense = (params)=>postAction("/saleOrderReturnExpense/add",params)
const editSaleOrderReturnExpense = (params)=>postAction("/saleOrderReturnExpense/edit",params)
const findSaleOrderReturnQueryDto = (params)=>getAction("/saleOrderReturn/queryDto",params)
const disableSaleOrderReturn = (params)=>postAction("/saleOrderReturn/disable",params)
const getSaleOrderReturnList = (params)=>getAction("/saleOrderReturn/getList",params)


// 采购批次
const addPurchaseBatch = (params)=>postAction("/purchaseBatch/add", params)
const getPurchaseBatchOne = (params)=>getAction("/purchaseBatch/getOne",params)
const editPurchaseBatch = (params)=>postAction("/purchaseBatch/edit",params)
const getPurchaseBatchList = (params)=>getAction("/purchaseBatch/getList",params)

// 库存
const getinventoryList = (params)=>getAction("/inventory/getList",params)

// 调拨单
const getAllotOne = (params)=>getAction("/allot/getOne",params)
const allotDtlDelete = (params)=>deleteAction("/allotDtl/delete",params)

// 组装单
const getAssembleOne = (params)=>getAction("/assemble/getOne",params)
const assembleDtlDelete = (params)=>deleteAction("/assembleDtl/delete",params)

// 拆卸单
const getTeardownOne = (params)=>getAction("/teardown/getOne",params)
const teardownDtlDelete = (params)=>deleteAction("/teardownDtl/delete",params)

// 工单
const getWorkOrderOne = (params)=>getAction("/workOrder/getOne",params)
const workOrderDtlDelete = (params)=>deleteAction("/workOrderDtl/delete",params)
const updateWorkOrderStatus = (params)=>putAction("/workOrder/updateStatus",params);

// 维修单
const addRepair = (params)=>postAction("/repairOrder/add", params)
const getRepairOne = (params)=>getAction("/repairOrder/getOne",params)
const editRepair = (params)=>postAction("/repairOrder/edit",params)
const getRepairList = (params)=>getAction("/repairOrder/getList",params)

// 换货单

const getChangeOrderOne = (params)=>getAction("/changeOrder/getOne",params)
const changeOrderDtlDelete = (params)=>deleteAction("/changeOrderDtl/delete",params)

// 换货单
const getLogisticsOrderOne = (params)=>getAction("/logisticsOrder/getOne",params)
const logisticsOrderDtlDelete = (params)=>deleteAction("/logisticsOrderDtl/delete",params)

// 服务机构
const saveServiceInstitution = (params)=>postAction("/serviceInstitution/save", params)
const getServiceInstitutionList =  (params)=>getAction("/serviceInstitution/getList",params)
const searchInstitution = (params)=>getAction("/serviceInstitution/search",params)


// 统计报表
const getReportList = (params)=>getAction("/report/getList",params) //统计报表 列表
const reportQueryById = (params)=>getAction("/report/queryById",params) // 查询单个统计报表信息
const reportDelete = (params)=>deleteAction("/report/delete",params)  // 删除统计报表信息


// 地址
const addAddress = (params)=>postAction("/address/add", params)
const getAddressOne = (params)=>getAction("/address/getOne",params)
const editAddress = (params)=>postAction("/address/edit",params)
const getAddressList = (params)=>getAction("/address/getList",params)

// 收款单
const createReceiptOrder = (params)=>postAction("/receiptOrder/create", params)
const addReceiptOrderDtl = (params)=>postAction("/receiptOrderDtl/add", params)
const editReceiptOrderDtl = (params)=>postAction("/receiptOrderDtl/edit", params)
const getReceiptOrderDtlList = (params)=>getAction("/receiptOrderDtl/getList", params)

// 退款单
const addRefundOrderDtl = (params)=>postAction("/refundOrderDtl/add", params)
const editRefundOrderDtl = (params)=>postAction("/refundOrderDtl/edit", params)
const getRefundOrderDtlList = (params)=>getAction("/refundOrderDtl/getList", params)

// 销售订单地址
const saveOrderAddress = (params)=>postAction("/saleOrderAddress/save", params)
const getOrderAddress = (params)=>getAction("/saleOrderAddress/getOneBySourceId", params)

// 发票
const getInvoiceById = (params)=>getAction("/invoice/getOne", params)
const addInvoice = (params)=>asynPostAction("/invoice/add", params)
const getInvoiceAddress = (params)=>getAction("/invoiceAddress/getOneBySourceId", params)
const saveInvoiceAddress = (params)=>postAction("/invoiceAddress/save", params)
const createInvoice = (params)=>postAction("/invoice/createInvoice", params)

// 销售渠道
const getRootChannel = (params)=>getAction("/saleOrderChannel/getRootChannel",params);
const getSubChannel = (params)=>getAction("/saleOrderChannel/getSubChannel",params);
const queryChannelTreeList = (params)=>getAction("/saleOrderChannel/queryChannelTreeList",params);

const addSaleOrderChannel= (params)=>postAction("/saleOrderChannel/add",params);
const editSaleOrderChannel= (params)=>putAction("/saleOrderChannel/edit",params);
const getSaleOrderChannel = (params)=>getAction("/saleOrderChannel/list",params);

const getWorkAddress = (params)=>getAction("/workAddress/getOneBySourceId", params);
const saveWorkAddress = (params)=>postAction("/workAddress/save", params);
const getInstallAddress = (params)=>getAction("/workAddress/getInstallAddress", params);

const getTaobaoOrder = (params)=>getAction("/saleOrder/getTaobaoOrder", params);

const subscribe = (params)=>getAction("/taoBaoSetting/subscribe", params);
const getOneTrade = (params)=>getAction("/slTrade/queryFromTaoBao", params);
const getSlTradeOrderList = (params)=>getAction("/slTradeOrder/list", params);

export {
  getOneTrade,

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
  getDepartById,
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
  editSaleOrder,
  getSaleOrderOne,
  getSaleOrderByCode,
  disableSaleOrder,
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
  getWarehouseList,
  getAreaList,
  getAreaOne,
  saveCustomer,
  getCustomerOne,
  getDeliveryInfo,
  addVendor,
  editVendor,
  getVendorList,
  addMaterial,
  editMaterial,
  getMaterialTypeList,
  getMaterialBrandList,
  getMaterialUnitList,
  getMaterialUnitListByIds,
  getMaterialOne,
  getMaterialSelfUnitList,
  addMaterialSelfUnit,
  editMaterialSelfUnit,
  getMaterialList,
  getMaterialListByIds,
  searchMaterial,
  addMaterialPrice,
  editMaterialPrice,
  getCustomerList,
  addSaleMtlOrder,
  editSaleMtlOrder,
  getlSaleMtlList,
  getlSaleMtlOneByMtlId,
  getDiscountTypeList,
  getMtlPrice,
  addExpense,
  editExpense,
  getExpenseList,
  addSaleOrderCost,
  editSaleOrderCost,
  getSaleOrderCostList,
  addSaleOrderExpense,
  editSaleOrderExpense,
  getSaleOrderExpenseList,
  delivery,
  getSaleOrderDeliveryInfo,
  getDeliveryMtls,
  getPutInMtls,
  addStocking,
  editStocking,
  handleStocking,
  getBillTypeList,
  getDateFmtList,
  addBillCodeBuilder,
  editBillCodeBuilder,
  deliveryStockOut,
  putStockIn,
  viewInventoryLog,
  purchasequeryById,
  purchasedetailDelete,
  inventoryInedit,
  inventoryOutSave,
  purchasereturnadd,
  purchasereturnedit,
  getPurchaseByCode,
  addLogisticsCompany,
  editLogisticsCompany,
  getLogisticsCompanyList,
  addSaleOrderReturn,
  editSaleOrderReturn,
  saleOrderReturncheckIn,
  getSaleOrderReturnOne,
  addSaleMtlOrderReturn,
  editSaleMtlOrderReturn,
  getSaleOrderReturnMtlList,
  getSaleOrderExpenseOne,
  addSaleOrderReturnExpense,
  editSaleOrderReturnExpense,
  getPurchaseMtlList,
  getPurchaseMtlOne,
  purchaseReturnQueryById,
  purchaseReturnDetailDelete,
  searchCustomer,
  getDeliveryInfoBySourceId,
  addPurchaseBatch,
  getPurchaseBatchOne,
  editPurchaseBatch,
  getPurchaseBatchList,
  getinventoryList,
  getAllotOne,
  allotDtlDelete,
  getAssembleOne,
  assembleDtlDelete,
  getTeardownOne,
  teardownDtlDelete,
  getWorkOrderOne,
  workOrderDtlDelete,
  updateWorkOrderStatus,
  addRepair,
  editRepair,
  getRepairOne,
  getRepairList,
  getChangeOrderOne,
  changeOrderDtlDelete,
  getLogisticsOrderOne,
  logisticsOrderDtlDelete,
  saveServiceInstitution,
  getServiceInstitutionList,
  searchInstitution,
  handleSaleOrderCost,
  getAddressTypeList,
  addAddress,
  editAddress,
  getAddressOne,
  getAddressList,
  createReceiptOrder,
  getPayModeTypeList,
  addReceiptOrderDtl,
  editReceiptOrderDtl,
  getReceiptOrderDtlList,
  getDeliveryTypeList,
  getReportList,
  reportQueryById,
  getWorkTypeList,
  saveOrderAddress,
  getOrderAddress,
  getInvoiceTypeList,
  getInvoiceTextureList,
  getInvoiceById,
  getInvoiceAddress,
  addInvoice,
  saveInvoiceAddress,
  createInvoice,
  findSaleOrderReturnQueryDto,
  getReturnTypeList,
  addRefundOrderDtl,
  editRefundOrderDtl,
  getRefundOrderDtlList,
  getPriceSpaceModeList,
  inventoryInSave,
  reportDelete,
  getRootChannel,
  getSubChannel,
  queryChannelTreeList,
  addSaleOrderChannel,
  editSaleOrderChannel,
  getSaleOrderChannel,
  disableSaleOrderReturn,
  getSaleOrderReturnList,
  getWorkAddress,
  saveWorkAddress,
  getInstallAddress,
  getTaobaoOrder,
  subscribe,
  getSlTradeOrderList,
  addSupplementary,
  editSupplementary,
  addSupplementaryValue,
  editSupplementaryValue,
  addMaterialAuxiliary,
  editMaterialAuxiliary,
  addMaterialAuxiliaryItem,
  editMaterialAuxiliaryItem,
  getSupplementaryOne,
  getSupplementaryList,
  getSupplementaryValListBySourceCode,
  getMaterialAuxiliaryList,
  getMaterialAuxiliaryListBySourceIds
}



