package com.daimontech.dsapi.product.service;

import com.daimontech.dsapi.product.model.Colors;
import com.daimontech.dsapi.product.model.Packages;
import com.daimontech.dsapi.product.repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServiceImpl implements ColorService {

    @Autowired
    ColorRepository colorRepository;

    public Boolean existsByColorId(Long colorId) {
        return colorRepository.existsById(colorId);
    }

    public Boolean existsByColorCode(String colorCode) {
        return colorRepository.existsByColorCode(colorCode);
    }

    public Boolean addNewColor(Colors colors) {
        try {
            colorRepository.save(colors);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Colors getColorsById(Long colorId) {
        return colorRepository.getById(colorId);
    }

    public List<Colors> getAllColors() { return colorRepository.findAllByColorCodeNotNull();}

    public Boolean delete(Colors colors) {
        try {
            colorRepository.delete(colors);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
