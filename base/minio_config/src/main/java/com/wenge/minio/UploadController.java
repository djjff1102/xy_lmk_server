package com.wenge.minio;

import com.wenge.common.constants.Constants;
import com.wenge.common.exception.BusinessException;
import com.wenge.common.result.PageResult;
import com.wenge.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HAO灏 2022/11/17 13:05
 */
@Tag(name = "上传下载接口")
@Validated
@RestController
public class UploadController {


	@Operation(summary = "上传文件", method = "POST")
	@PostMapping("/upload")
	public Result<String> upload(@Parameter(description = "文件") @RequestParam("file") MultipartFile file) throws Exception {
		this.checkFileSize(file);
		return Result.success(MinioUtil.upload(file));
	}

	@Operation(summary = "批量上传文件", method = "POST")
	@PostMapping("/batch/upload")
	public PageResult<String> upload(@Parameter(description = "文件") @RequestParam("files") MultipartFile[] files) throws Exception {
		List<String> urls = new ArrayList<>();
		for (MultipartFile file : files) {
			this.checkFileSize(file);
		}
		for (MultipartFile file : files) {
			urls.add(MinioUtil.upload(file));
		}
		return PageResult.success(urls);
	}

	private void checkFileSize(MultipartFile file) {
		long size = file.getSize();
		if (size > Constants.MAX_FILE_SIZE) {
			throw new BusinessException("file_size_limit");
		}
	}

	@Operation(summary = "下载文件", method = "GET")
	@GetMapping("/download/{file_name}")
	public void download(@Parameter(description = "文件名") @PathVariable("file_name") String fileName, HttpServletResponse response) throws Exception {
		MinioUtil.download(response, fileName);
	}

}
