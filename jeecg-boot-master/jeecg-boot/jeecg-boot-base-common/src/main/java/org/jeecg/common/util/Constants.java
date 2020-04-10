package org.jeecg.common.util;

import org.jeecg.common.enums.BillType;
import org.jeecg.common.enums.InventoryOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类
 */
public class Constants {

    // md5
    public static final String SIGN_METHOD_MD5 = "md5";
    // hmac
    public static final String SIGN_METHOD_HMAC = "hmac";
    // utf-8
    public static final String CHARSET_UTF8 = "utf-8";
    //gzip
    public static final String CONTENT_ENCODING_GZIP = "gzip";

    public static final Map<Integer, Integer> OUTOPERATIONS = new HashMap<Integer, Integer>();
    public static final Map<Integer, Integer> INOPERATIONS = new HashMap<Integer, Integer>();

    static {
        OUTOPERATIONS.put(BillType.SALEORDER.getId(), InventoryOperation.SALEOUT.getId());
        OUTOPERATIONS.put(BillType.PURCHASERETURNORDER.getId(), InventoryOperation.PURCHASERETURNOUT.getId());
        OUTOPERATIONS.put(BillType.ALLOT.getId(), InventoryOperation.ALLOTOUT.getId());
        OUTOPERATIONS.put(BillType.ASSEMBLE.getId(), InventoryOperation.ASSEMBLEOUT.getId());
        OUTOPERATIONS.put(BillType.TEARDOWN.getId(), InventoryOperation.TEARDOWNOUT.getId());

        INOPERATIONS.put(BillType.SALERETURNORDER.getId(), InventoryOperation.SALERETURNIN.getId());
        INOPERATIONS.put(BillType.PURCHASEORDER.getId(), InventoryOperation.PURCHASEIN.getId());
        INOPERATIONS.put(BillType.ALLOT.getId(), InventoryOperation.ALLOTIN.getId());
        INOPERATIONS.put(BillType.ASSEMBLE.getId(), InventoryOperation.ASSEMBLEIN.getId());
        INOPERATIONS.put(BillType.TEARDOWN.getId(), InventoryOperation.TEARDOWNIN.getId());
    }
}
