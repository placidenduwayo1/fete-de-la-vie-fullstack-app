package com.fdlv.gmd.api.service.forum.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fdlv.gmd.api.service.forum.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdlv.gmd.api.domain.forum.Document;
import com.fdlv.gmd.api.dto.forum.DocumentDTO;
import com.fdlv.gmd.api.mapper.forum.DocumentMapper;
import com.fdlv.gmd.api.repository.forum.DocumentRepository;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentServiceImpl  implements DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentMapper documentMapper ) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);
        Document document = documentMapper.toEntity(documentDTO);
        document = documentRepository.save(document);
        return documentMapper.toDto(document);
    }

    @Override
    public Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO) {
        log.debug("Request to partially update Document : {}", documentDTO);

        return documentRepository
            .findById(documentDTO.getId())
            .map(
                existingDocument -> {
                    documentMapper.partialUpdate(existingDocument, documentDTO);
                    return existingDocument;
                }
            )
            .map(documentRepository::save)
            .map(documentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDTO> findAll() {
        log.debug("Request to get all Documents");
        return documentRepository.findAll().stream().map(documentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id).map(documentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return documentRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
    }
}
