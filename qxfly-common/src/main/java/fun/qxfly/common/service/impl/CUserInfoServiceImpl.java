package fun.qxfly.common.service.impl;

import fun.qxfly.common.domain.entity.Token;
import fun.qxfly.common.mapper.CUserInfoMapper;
import fun.qxfly.common.service.CUserInfoService;
import org.springframework.stereotype.Service;

@Service
public class CUserInfoServiceImpl implements CUserInfoService {
    final CUserInfoMapper cUserInfoMapper;

    public CUserInfoServiceImpl(CUserInfoMapper cUserInfoMapper) {
        this.cUserInfoMapper = cUserInfoMapper;
    }

    /**
     * 刷新用户信息
     *
     * @param uid
     * @return
     */
    @Override
    public boolean refreshUserInfoTask(Integer uid) {
        Integer articleCount = cUserInfoMapper.getArticleCount(uid);
        Integer LikeCount = cUserInfoMapper.getLikeCount(uid);
        Integer Collection = cUserInfoMapper.getCollectionCount(uid);
        Integer Views = cUserInfoMapper.getViewsCount(uid);
        if (articleCount == null) {
            articleCount = 0;
        }
        if (LikeCount == null) {
            LikeCount = 0;
        }
        if (Collection == null) {
            Collection = 0;
        }
        if (Views == null) {
            Views = 0;
        }
        return cUserInfoMapper.refreshUserInfo(articleCount, LikeCount, Collection, Views, 0, uid);
    }
}
