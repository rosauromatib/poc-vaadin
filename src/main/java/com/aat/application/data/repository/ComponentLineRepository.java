package com.aat.application.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aat.application.data.entity.ZJTComponentLine;
import com.aat.application.data.entity.ZJTProduct;

public interface ComponentLineRepository extends JpaRepository<ZJTComponentLine, Integer>
{

	List<ZJTComponentLine> findByParentIs(ZJTProduct product);
}
