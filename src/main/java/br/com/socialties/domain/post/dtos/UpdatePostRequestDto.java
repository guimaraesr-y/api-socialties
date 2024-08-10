package br.com.socialties.domain.post.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public record UpdatePostRequestDto (

        Optional<String> title,
        Optional<String> description,
        Optional<List<MultipartFile>> contents

) { }
