package com.kinglian.screeninquiry.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.kinglian.screeninquiry.dao.MedOfficeVisitMapper;
import com.kinglian.screeninquiry.model.entity.MedOfficeVisit;
import com.kinglian.screeninquiry.service.MedOfficeVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HXC
 * @date 2019-1-18
 */
@Service
@Transactional
public class MedOfficeVisitServiceImpl extends ServiceImpl<MedOfficeVisitMapper, MedOfficeVisit> implements MedOfficeVisitService {
    @Autowired
    private MedOfficeVisitMapper medOfficeVisitMapper;

    /**
     * 医生端接诊操作
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean clinicalReception(String orderId) {
        MedOfficeVisit medOfficeVisit = new MedOfficeVisit();
        medOfficeVisit.setVisitStatus("1");
        return medOfficeVisitMapper.update(medOfficeVisit, new EntityWrapper<MedOfficeVisit>().eq("visitid", orderId))>0;
    }
}
