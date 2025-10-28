package fun.qxfly.mapper;

import fun.qxfly.common.domain.entity.Navigation;
import fun.qxfly.common.domain.entity.Site;
import fun.qxfly.common.domain.entity.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarehouseMapper {
    /**
     * 分页查询站点
     *
     * @param name
     * @return
     */
    List<Warehouse> listInventory(String name, Integer shelf, Integer layer);


    /**
     * 入库
     *
     * @param warehouse
     * @return
     */
    @Insert("INSERT INTO warehouse(shelf,layer,name,quantity,picture)values(#{shelf},#{layer},#{name},#{quantity},#{picture})")
    Integer warehousing(Warehouse warehouse);

    /**
     * 删除库存
     *
     * @param warehouse
     * @return
     */
    @Delete("DELETE FROM warehouse WHERE id = #{id}")
    Integer deleteStock(Warehouse warehouse);

    /**
     * 列出所有库存
     *
     * @return
     */
    @Select("select * from warehouse")
    List<Warehouse> listAllStock();

    /**
     * 修改库存
     * @param warehouse
     * @return
     */
    @Update("UPDATE warehouse SET shelf = #{shelf},layer = #{layer},name = #{name},quantity = #{quantity},picture = #{picture} WHERE id = #{id}")
    Integer editWarehouse(Warehouse warehouse);
}
