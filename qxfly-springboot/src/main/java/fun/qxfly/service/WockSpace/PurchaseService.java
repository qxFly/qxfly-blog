package fun.qxfly.service.WockSpace;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Purchase;
import org.springframework.web.multipart.MultipartFile;

public interface PurchaseService {
    /**
     * 列出所有采购项
     * @param currPage
     * @param pageSize
     * @param applicant
     * @param item
     * @param status
     * @return
     */
    PageInfo<Purchase> listItems(Integer currPage, Integer pageSize, String applicant, String reporting,  String reported,Integer status, String month);

    /**
     * 添加采购项
     * @param purchase
     * @return
     */
    Integer addItem(Purchase purchase);

    /**
     * 编辑采购项
     * @param purchase
     * @return
     */
    Integer editItem(Purchase purchase);

    /**
     * 删除采购项
     * @param purchase
     * @return
     */
    Integer deleteItem(Purchase purchase);

    /**
     * 上传图片
     * @param file
     * @return
     */
    String uploadPicture(MultipartFile file);
}
