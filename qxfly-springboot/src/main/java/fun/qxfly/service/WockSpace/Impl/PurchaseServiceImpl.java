package fun.qxfly.service.WockSpace.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.common.domain.entity.Purchase;
import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.enums.FilePaths;
import fun.qxfly.common.exception.excep.FileException;
import fun.qxfly.common.utils.FileUtils;
import fun.qxfly.mapper.WorkSpace.PurchaseMapper;
import fun.qxfly.service.WockSpace.PurchaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    @Value("${qxfly.file.path.purchasePicture}")
    private String purchasePicturePath;

    private final PurchaseMapper purchaseMapper;

    public PurchaseServiceImpl(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    /**
     * 列出采购项目
     *
     * @param currPage
     * @param pageSize
     * @param applicant
     * @param item
     * @param status
     * @return
     */
    @Override
    public PageInfo<Purchase> listItems(Integer currPage, Integer pageSize, String applicant, String reporting, String reported, Integer status, String month) {
        PageHelper.startPage(currPage, pageSize);
        List<Purchase> list = purchaseMapper.listItems(applicant, reporting, reported, status, month);
        list.forEach(i -> {
            i.setPicture(purchasePicturePath + i.getPicture());
        });
        return new PageInfo<>(list);
    }

    /**
     * 添加采购项目
     *
     * @param purchase
     * @return
     */
    @Override
    public Integer addItem(Purchase purchase) {
        return purchaseMapper.addItems(purchase);
    }

    /**
     * 编辑采购项目
     *
     * @param purchase
     * @return
     */
    @Override
    public Integer editItem(Purchase purchase) {
        Purchase itemById = purchaseMapper.getItemById(purchase.getId());
        String oldPic = itemById.getPicture();
        String[] split = purchase.getPicture().split("purchasePicture/");
        String newPic = "";
        if (split.length > 1) {
            newPic = split[1];
            purchase.setPicture(newPic);
        }
        if (!Objects.equals(oldPic, newPic)) {
            /* 删除封面 */
            String path = FilePaths.PURCHASE_PICTURE_PATH.getPath() + oldPic;
            path = FileUtils.toSystemSeparator(path);
            if (path != null) {
                File cover = new File(path);
                if (cover.exists()) cover.delete();
            }

        }

        return purchaseMapper.editItems(purchase);
    }

    /**
     * 删除采购项目
     *
     * @param purchase
     * @return
     */
    @Override
    public Integer deleteItem(Purchase purchase) {
        Purchase p = purchaseMapper.getItemById(purchase.getId());
        /* 删除封面 */
        String path = FilePaths.PURCHASE_PICTURE_PATH.getPath() + p.getPicture();
        path = FileUtils.toSystemSeparator(path);
        File cover = new File(path);
        if (cover.exists()) cover.delete();
        return purchaseMapper.deleteItem(purchase);
    }

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadPicture(MultipartFile file) {
        try {
            return FileUtils.upload(FilePaths.PURCHASE_PICTURE_PATH.getPath(), file);
        } catch (Exception e) {
            throw new FileException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }
}
