package fun.qxfly.common.enums;

public enum PurchaseStatus {
    Waiting_Report(0, "待提报"),
    Apply_AssetNumber(1, "申请资产号"),
    Partial_Report(2, "部分提报"),
    Reported(3, "已提报"),
    FinishD(4, "已发放"),
    ;

    PurchaseStatus(int i, String status) {

    }

    public static String getStatus(int i) {
        return null;
    }
}
