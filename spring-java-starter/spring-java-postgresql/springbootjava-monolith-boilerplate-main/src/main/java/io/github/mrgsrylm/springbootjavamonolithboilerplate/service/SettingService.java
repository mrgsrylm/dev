package io.github.mrgsrylm.springbootjavamonolithboilerplate.service;

import io.github.mrgsrylm.springbootjavamonolithboilerplate.entity.Setting;
import io.github.mrgsrylm.springbootjavamonolithboilerplate.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final SettingRepository settingRepository;

    public Setting create() {
        return settingRepository.save(Setting.builder().key("foo").value("bar").build());
    }
}
