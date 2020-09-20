package org.jeecg.common.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 淘宝开放接口常量
 * @Author sandylee
 *
 */
public interface TaobaoMessageConstant {

	public static final List<String> TAOBAO_MESSAGE_LIST = Lists.newArrayList(
			"taobao_trade_TradeCreate",
			"taobao_trade_TradeModifyFee",
			"taobao_trade_TradeCloseAndModifyDetailOrder",
			"taobao_trade_TradeClose",
			"taobao_trade_TradeBuyerPay",
			"taobao_trade_TradeSellerShip",
			"taobao_trade_TradeDelayConfirmPay",
			"taobao_trade_TradePartlyRefund",
			"taobao_trade_TradePartlyConfirmPay",
			"taobao_trade_TradeSuccess",
			"taobao_trade_TradeTimeoutRemind",
			"taobao_trade_TradeRated",
			"taobao_trade_TradeMemoModified",
			"taobao_trade_TradeLogisticsAddressChanged",
			"taobao_trade_TradeChanged", //
			"taobao_trade_TradeAlipayCreate",
			"taobao_trade_TradePartlySellerShip",
			"taobao_refund_RefundCreated",
			"taobao_refund_RefundSellerAgreeAgreement",
			"taobao_refund_RefundSellerRefuseAgreement",
			"taobao_refund_RefundBuyerModifyAgreement",
			"taobao_refund_RefundBuyerReturnGoods",
			"taobao_refund_RefundCreateMessage",
			"taobao_refund_RefundBlockMessage",
			"taobao_refund_RefundClosed",
			"taobao_refund_RefundSuccess",
			"taobao_item_ItemAdd",
			"taobao_item_ItemUpshelf",
			"taobao_item_ItemDownshelf",
			"taobao_item_ItemDelete",//
			"taobao_item_ItemUpdate",
			"taobao_item_ItemRecommendAdd",
			"taobao_item_ItemZeroStock",
			"taobao_item_ItemPunishDelete",
			"taobao_item_ItemPunishDownshelf",
			"taobao_item_ItemPunishCc",
			"taobao_item_ItemSkuZeroStock",
			"taobao_item_ItemStockChanged",
			"taobao_fenxiao_FxOrderCreate",
			"taobao_fenxiao_FxOrderModifyPrice",
			"taobao_fenxiao_FxOrderClosed",
			"taobao_fenxiao_FxOrderPaid",
			"taobao_fenxiao_FxOrderShipped",
			"taobao_fenxiao_FxOrderSuccess",
			"taobao_fenxiao_FxRefundCreate",//
			"taobao_fenxiao_FxRefundRefuseGoods",
			"taobao_fenxiao_FxRefundAgree",
			"taobao_fenxiao_FxRefundSuccess",
			"taobao_fenxiao_FxTradeRefundCreate",
			"taobao_fenxiao_FxTradeRefundSuccess",
			"taobao_item_ItemRecommendDelete",
			"taobao_fenxiao_DealerCreate",
			"taobao_fenxiao_DealerClose",
			"taobao_fenxiao_DealerModify",
			"taobao_fenxiao_DealerInstock",
			"taobao_fenxiao_DealerShipped",
			"taobao_fenxiao_DealerConfirm",
			"taobao_fenxiao_DealerPay",
			"taobao_fenxiao_DealerRefuse",
			"taobao_fenxiao_DealerAgree",//
			"taobao_refund_TaobaoInterApplied",
			"taobao_refund_TaobaoIntervened",
			"taobao_refund_RefundableMarked",
			"taobao_refund_RefundableCanceled",
			"taobao_trade_TradeBuyerStepPay",
			"taobao_fuwu_OrderPaid",
			"taobao_logistics_LogsticDetailTrace",
			"taobao_fenxiao_FxRefundClose",
			"taobao_fuwu_OrderCreated",
			"taobao_fuwu_OrderClosed",
			"fuwu_confirm_Success",
			"fuwu_confirm_Fail",
			"tmall_channel_ApplyOrderChange",
			"tmall_channel_PurchaseOrderChange",
			"tmall_channel_DeliverOrderChange", //
			"tmall_channel_StopOrderChange",
			"taobao_fenxiao_FxOrderModifyConsign",
			"taobao_top_AuthCancel",
			"wdk_market_OperateSkuErrror",
			"wdk_market_OperateSkuError",
			"wdk_open_DataRelation",
			"wdk_open_DataPublishError"
			); //消息列表


	public static final String TAOBAO_TRADE_TRADECREATE = "taobao_trade_TradeCreate"; // 订单创建消息
	public static final String TAOBAO_TRADE_TRADEMODIFYFEE = "taobao_trade_TradeModifyFee";
	public static final String TAOBAO_TRADE_TRADECLOSEANDMODIFYDETAILORDER = "taobao_trade_TradeCloseAndModifyDetailOrder";
	public static final String TAOBAO_TRADE_TRADECLOSE = "taobao_trade_TradeClose";
	public static final String TAOBAO_TRADE_TRADEBUYERPAY = "taobao_trade_TradeBuyerPay"; //买家付完款，或万人团买家付完尾款
	public static final String TAOBAO_TRADE_TRADESELLERSHIP = "taobao_trade_TradeSellerShip";
	public static final String TAOBAO_TRADE_TRADEDELAYCONFIRMPAY = "taobao_trade_TradeDelayConfirmPay";
	public static final String TAOBAO_TRADE_TRADEPARTLYREFUND = "taobao_trade_TradePartlyRefund";
	public static final String TAOBAO_TRADE_TRADEPARTLYCONFIRMPAY = "taobao_trade_TradePartlyConfirmPay";
	public static final String TAOBAO_TRADE_TRADESUCCESS = "taobao_trade_TradeSuccess"; //交易成功消息
	public static final String TAOBAO_TRADE_TRADETIMEOUTREMIND = "taobao_trade_TradeTimeoutRemind";
	public static final String TAOBAO_TRADE_TRADERATED = "taobao_trade_TradeRated";
	public static final String TAOBAO_TRADE_TRADEMEMOMODIFIED = "taobao_trade_TradeMemoModified";
	public static final String TAOBAO_TRADE_TRADELOGISTICSADDRESSCHANGED = "taobao_trade_TradeLogisticsAddressChanged";
	public static final String TAOBAO_TRADE_TRADECHANGED = "taobao_trade_TradeChanged"; //订单信息变更消息
	public static final String TAOBAO_TRADE_TRADEALIPAYCREATE = "taobao_trade_TradeAlipayCreate";
	public static final String TAOBAO_TRADE_TRADEPARTLYSELLERSHIP = "taobao_trade_TradePartlySellerShip";
	public static final String TAOBAO_REFUND_REFUNDCREATED = "taobao_refund_RefundCreated";
	public static final String TAOBAO_REFUND_REFUNDSELLERAGREEAGREEMENT = "taobao_refund_RefundSellerAgreeAgreement";
	public static final String TAOBAO_REFUND_REFUNDSELLERREFUSEAGREEMENT = "taobao_refund_RefundSellerRefuseAgreement";
	public static final String TAOBAO_REFUND_REFUNDBUYERMODIFYAGREEMENT = "taobao_refund_RefundBuyerModifyAgreement";
	public static final String TAOBAO_REFUND_REFUNDBUYERRETURNGOODS = "taobao_refund_RefundBuyerReturnGoods";
	public static final String TAOBAO_REFUND_REFUNDCREATEMESSAGE = "taobao_refund_RefundCreateMessage";
	public static final String TAOBAO_REFUND_REFUNDBLOCKMESSAGE = "taobao_refund_RefundBlockMessage";
	public static final String TAOBAO_REFUND_REFUNDCLOSED= "taobao_refund_RefundClosed";
	public static final String TAOBAO_REFUND_REFUNDSUCCESS = "taobao_refund_RefundSuccess";
	public static final String TAOBAO_ITEM_ITEMADD = "taobao_item_ItemAdd";
	public static final String TAOBAO_ITEM_ITEMUPSHELF = "taobao_item_ItemUpshelf";
	public static final String TAOBAO_ITEM_ITEMDOWNSHELF = "taobao_item_ItemDownshelf";
	public static final String TAOBAO_ITEM_ITEMDELETE = "taobao_item_ItemDelete";//
	public static final String TAOBAO_ITEM_ITEMUPDATE = "taobao_item_ItemUpdate";
	public static final String TAOBAO_ITEM_ITEMRECOMMENDADD = "taobao_item_ItemRecommendAdd";
	public static final String TAOBAO_ITEM_ITEMZEROSTOCK = "taobao_item_ItemZeroStock";
	public static final String TAOBAO_ITEM_ITEMPUNISHDELETE = "taobao_item_ItemPunishDelete";
	public static final String TAOBAO_ITEM_ITEMPUNISHDOWNSHELF = "taobao_item_ItemPunishDownshelf";
	public static final String TAOBAO_ITEM_ITEMPUNISHCC = "taobao_item_ItemPunishCc";
	public static final String TAOBAO_ITEM_ITEMSKUZEROSTOCK = "taobao_item_ItemSkuZeroStock";
	public static final String TAOBAO_ITEM_ITEMSTOCKCHANGED = "taobao_item_ItemStockChanged";
	public static final String TAOBAO_FENXIAO_FXORDERCREATE = "taobao_fenxiao_FxOrderCreate";
	public static final String TAOBAO_FENXIAO_FXORDERMODIFYPRICE = "taobao_fenxiao_FxOrderModifyPrice";
	public static final String TAOBAO_FENXIAO_FXORDERCLOSED = "taobao_fenxiao_FxOrderClosed";
	public static final String TAOBAO_FENXIAO_FXORDERPAID = "taobao_fenxiao_FxOrderPaid";
	public static final String TAOBAO_FENXIAO_FXORDERSHIPPED = "taobao_fenxiao_FxOrderShipped";
	public static final String TAOBAO_FENXIAO_FXORDERSUCCESS = "taobao_fenxiao_FxOrderSuccess";
	public static final String TAOBAO_FENXIAO_FXREFUNDCREATE = "taobao_fenxiao_FxRefundCreate";//
	public static final String TAOBAO_FENXIAO_FXREFUNDREFUSEGOODS = "taobao_fenxiao_FxRefundRefuseGoods";
	public static final String TAOBAO_FENXIAO_FXREFUNDAGREE = "taobao_fenxiao_FxRefundAgree";
	public static final String TAOBAO_FENXIAO_FXREFUNDSUCCESS = "taobao_fenxiao_FxRefundSuccess";
	public static final String TAOBAO_FENXIAO_FXTRADEREFUNDCREATE = "taobao_fenxiao_FxTradeRefundCreate";
	public static final String TAOBAO_FENXIAO_FXTRADEREFUNDSUCCESS = "taobao_fenxiao_FxTradeRefundSuccess";
	public static final String TAOBAO_ITEM_ITEMRECOMMENDDELETE = "taobao_item_ItemRecommendDelete";
	public static final String TAOBAO_FENXIAO_DEALERCREATE = "taobao_fenxiao_DealerCreate";
	public static final String TAOBAO_FENXIAO_DEALERCLOSE = "taobao_fenxiao_DealerClose";
	public static final String TAOBAO_FENXIAO_DEALERMODIFY = "taobao_fenxiao_DealerModify";
	public static final String TAOBAO_FENXIAO_DEALERINSTOCK = "taobao_fenxiao_DealerInstock";
	public static final String TAOBAO_FENXIAO_DEALERSHIPPED = "taobao_fenxiao_DealerShipped";
	public static final String TAOBAO_FENXIAO_DEALERCONFIRM = "taobao_fenxiao_DealerConfirm";
	public static final String TAOBAO_FENXIAO_DEALERPAY = "taobao_fenxiao_DealerPay";
	public static final String TAOBAO_FENXIAO_DEALERREFUSE = "taobao_fenxiao_DealerRefuse";
	public static final String TAOBAO_FENXIAO_DEALERAGREE = "taobao_fenxiao_DealerAgree";//
	public static final String TAOBAO_REFUND_TAOBAOINTERAPPLIED = "taobao_refund_TaobaoInterApplied";
	public static final String TAOBAO_REFUND_TAOBAOINTERVENED = "taobao_refund_TaobaoIntervened";
	public static final String TAOBAO_REFUND_REFUNDABLEMARKED = "taobao_refund_RefundableMarked";
	public static final String TAOBAO_REFUND_REFUNDABLECANCELED = "taobao_refund_RefundableCanceled";
	public static final String TAOBAO_TRADE_TRADEBUYERSTEPPAY = "taobao_trade_TradeBuyerStepPay";
	public static final String TAOBAO_FUWU_ORDERPAID = "taobao_fuwu_OrderPaid";
	public static final String TAOBAO_LOGISTICS_LOGSTICDETAILTRACE = "taobao_logistics_LogsticDetailTrace";
	public static final String TAOBAO_FENXIAO_FXREFUNDCLOSE = "taobao_fenxiao_FxRefundClose";
	public static final String TAOBAO_FUWU_ORDERCREATED = "taobao_fuwu_OrderCreated";
	public static final String TAOBAO_FUWU_ORDERCLOSED = "taobao_fuwu_OrderClosed";
	public static final String FUWU_CONFIRM_SUCCESS = "fuwu_confirm_Success";
	public static final String FUWU_CONFIRM_FAIL = "fuwu_confirm_Fail";
	public static final String TMALL_CHANNEL_APPLYORDERCHANGE = "tmall_channel_ApplyOrderChange";
	public static final String TMALL_CHANNEL_PURCHASEORDERCHANGE = "tmall_channel_PurchaseOrderChange";
	public static final String TMALL_CHANNEL_DELIVERORDERCHANGE = "tmall_channel_DeliverOrderChange"; //
	public static final String TMALL_CHANNEL_STOPORDERCHANGE = "tmall_channel_StopOrderChange";
	public static final String TAOBAO_FENXIAO_FXORDERMODIFYCONSIGN = "taobao_fenxiao_FxOrderModifyConsign";
	public static final String TAOBAO_TOP_AUTHCANCEL = "taobao_top_AuthCancel";
	public static final String WDK_MARKET_OPERATESKUERRROR = "wdk_market_OperateSkuErrror";
	public static final String WDK_MARKET_OPERATESKUERROR = "wdk_market_OperateSkuError";
	public static final String WDK_OPEN_DATARELATION = "wdk_open_DataRelation";
	public static final String WDK_OPEN_DATAPUBLISHERROR = "wdk_open_DataPublishError";
}
