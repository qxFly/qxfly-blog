package fun.qxfly.mapper.WorkSpace;

import fun.qxfly.common.domain.entity.Purchase;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PurchaseMapper {
    /**
     * 列出所有采购
     * @param applicant
     * @param item
     * @param status
     * @return
     */
    List<Purchase> listItems(String applicant, String reporting,  String reported, Integer status,String month);

    /**
     * 添加采购
     * @param purchase
     * @return
     */
    @Insert("insert into purchase(applicant,reporting,reported,status,picture,month) values(#{applicant},#{reporting},#{reported},#{status},#{picture},#{month})")
    Integer addItems(Purchase purchase);

    /**
     * 修改采购
     * @param purchase
     * @return
     */
    @Update("update purchase set applicant=#{applicant},reporting=#{reporting},reported=#{reported},status=#{status},picture=#{picture},month=#{month} where id=#{id}")
    Integer editItems(Purchase purchase);

    /**
     * 删除采购
     * @param purchase
     * @return
     */
    @Delete("delete from purchase where id=#{id}")
    Integer deleteItem(Purchase purchase);

    /**
     * 根据id查询采购
     * @param id
     * @return
     */
    @Select("select * from purchase where id=#{id}")
    Purchase getItemById(Integer id);
}
