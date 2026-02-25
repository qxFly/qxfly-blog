package fun.qxfly.service.WockSpace;

import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Site;
import fun.qxfly.common.domain.entity.Warehouse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WarehouseService {
    /**
     * 列出网站
     *
     * @return
     */
    PageInfo<Warehouse> listInventory(Integer currPage, Integer pageSize, String name, Integer shelf, Integer layer) ;

    /**
     * 获取站点状态
     * @param sites
     * @return
     */
    List<Site> getSiteStatus(List<Site> sites);

    /**
     * 入库
     * @param warehouse
     * @return
     */
    Integer warehousing(Warehouse warehouse);

    /**
     * 删除库存
     * @param warehouse
     * @return
     */
    Integer deleteStock(Warehouse warehouse);

    /**
     * 获取所有库存
     * @return
     */
    List<Warehouse> listAllStock();

    /**
     * 修改库存
     * @param warehouse
     * @return
     */
    Integer editWarehouse(Warehouse warehouse);

    /**
     * 图片上传
     * @param file
     * @return
     */
    String uploadPicture(MultipartFile file);
}
