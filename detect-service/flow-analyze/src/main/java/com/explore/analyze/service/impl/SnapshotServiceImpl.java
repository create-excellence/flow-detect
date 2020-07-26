package com.explore.analyze.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.explore.analyze.form.SnapshotQuery;
import com.explore.analyze.mappers.SnapshotMapper;
import com.explore.analyze.service.IFlowService;
import com.explore.analyze.service.ISnapshotService;
import com.explore.analyze.vo.SnapshotVo;
import com.explore.common.ServerResponse;
import com.explore.common.database.Flow;
import com.explore.common.database.Snapshot;
import com.explore.service.ICameraService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @ClassName SnapshotServiceImpl
 * @Description TODO
 * @Author 安羽兮
 * @Date 2020/7/1518:07
 * @Version 1.0
 **/
@Service
public class SnapshotServiceImpl extends ServiceImpl<SnapshotMapper, Snapshot> implements ISnapshotService {

    @Value("${spring.uploadPath}")
    private String uploadPath;

    private final ResourceLoader resourceLoader;

    private final IFlowService flowService;

    private final ICameraService cameraService;

    public SnapshotServiceImpl(ResourceLoader resourceLoader, IFlowService flowService, ICameraService cameraService) {
        this.resourceLoader = resourceLoader;
        this.flowService = flowService;
        this.cameraService = cameraService;
    }


    @Override
    public Page<SnapshotVo> pageByQuery(SnapshotQuery query) {
        Page page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<Snapshot> queryWrapper = new LambdaQueryWrapper();
        Page data = page(page, queryWrapper);
        List<SnapshotVo> snapshotVos = new ArrayList<>();
        data.getRecords().stream().forEach(snapshot -> {
            SnapshotVo snapshotVo = new SnapshotVo();
            BeanUtils.copyProperties(snapshot, snapshotVo);
            Flow flow = flowService.getById(snapshotVo.getFlowId());
            snapshotVo.setFlow(flow);
            if (null != flow)
                snapshotVo.setCamera(cameraService.getById(flow.getCameraId()));
            // 满足筛选条件才加入集合
            if (null == query.getFlow() || flow.getFlow().intValue() >= query.getFlow().intValue()) {
                if (null == query.getOrganizationId() || snapshotVo.getCamera().getOrganizationId().equals(query.getOrganizationId()))
                    snapshotVos.add(snapshotVo);
            } else {
                snapshotVo = null;
            }
        });
        data.setRecords(snapshotVos);
        return data;
    }

    @Override
    public ServerResponse upload(MultipartFile file) {
        // 图片路径
        String imgUrl = null;
        //上传
        try {
            String filename = upload(file, uploadPath, file.getOriginalFilename());
            Map result = new HashMap<>();
            result.put("path", filename);
            return ServerResponse.createBySuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("上传失败");
        }
    }

    @Override
    public ResponseEntity show(String fileName) {
        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + uploadPath + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String upload(MultipartFile file, String path, String fileName) throws Exception {
        // 生成新的文件名
        String realPath = path + "/" + UUID.randomUUID().toString().replace("-", "") + fileName.substring(fileName.lastIndexOf("."));
        File dest = new File(realPath);
        // 判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        // 保存文件
        file.transferTo(dest);
        return dest.getName();
    }
}
