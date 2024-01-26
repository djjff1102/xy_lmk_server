package com.wenge.datasource.base;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wenge.common.constants.Constants;
import com.wenge.common.exception.BusinessException;
import com.wenge.common.util.CommonUtil;

import java.io.Serializable;
import java.util.*;

/**
 * @author HAO灏 2022/11/8 14:20
 */
public class BaseService<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {

	@Override
	public boolean save(T entity) {
		Date now = new Date();
//		entity.setId(null);
		if (entity.getCreateDate() == null) {
			entity.setCreateDate(now);
		}
		if (entity.getModifyDate() == null) {
			entity.setModifyDate(now);
		}
		return super.save(entity);
	}

	@Override
	public boolean updateById(T entity) {
		this.checkSystemId(entity);
//		if (entity.getModifyDate() == null) {
			entity.setModifyDate(new Date());
//		}
		return super.updateById(entity);
	}

	public void checkSystemId(T entity){
		if(entity.isAllowModifySystemData()){
			return;
		}
		this.checkSystemId(entity.getId());
	}

	public void checkSystemId(long id){
		if (id < Constants.DEFAULT_SYSTEM_ID_MAX) {
			throw new BusinessException("can_not_delete_system_data");
		}
	}

	//@Override
	//public boolean saveBatch(Collection<T> entityList, int batchSize) {
	//	return super.saveBatch(entityList, batchSize);
	//}

	@Override
	public boolean saveBatch(Collection<T> entityList) {
		if (CommonUtil.isEmpty(entityList)) {
			return true;
		}
		Date now = new Date();
		entityList.parallelStream().forEach(entity -> {
//			entity.setId(null);
			if (entity.getCreateDate() == null) {
				entity.setCreateDate(now);
			}
			if (entity.getModifyDate() == null) {
				entity.setModifyDate(now);
			}
		});
		return super.saveBatch(entityList);
	}

	@Override
	public boolean updateBatchById(Collection<T> entityList) {
		if (CommonUtil.isEmpty(entityList)) {
			return true;
		}
		Date now = new Date();
		entityList.parallelStream().forEach(entity -> {
			this.checkSystemId(entity);
			if (entity.getCreateDate() == null) {
				entity.setCreateDate(now);
			}
//			if (entity.getModifyDate() == null) {
				entity.setModifyDate(now);
//			}
		});
		return super.updateBatchById(entityList);
	}

	public boolean removeById(Long id) {
		this.checkSystemId(id);
		return super.removeById(id);
	}

	public boolean removeByIds(List<Long> ids) {
		if (CommonUtil.isEmpty(ids)) {
			return true;
		}
		return this.removeByIds(new HashSet<>(ids));
	}

	public boolean removeByIds(Set<Long> ids) {
		if (CommonUtil.isEmpty(ids)) {
			return true;
		}
		ids.parallelStream().forEach(this::checkSystemId);
		return super.removeByIds(ids);
	}

	public List<T> listByIds(Collection<? extends Serializable> idList) {
		if (CommonUtil.isEmpty(idList)) {
			return CommonUtil.emptyList();
		}
		return super.listByIds(idList);
	}
}
