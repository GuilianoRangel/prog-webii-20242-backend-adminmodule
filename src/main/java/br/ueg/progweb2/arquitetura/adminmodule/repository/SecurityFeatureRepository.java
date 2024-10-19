package br.ueg.progweb2.arquitetura.adminmodule.repository;

import br.ueg.progweb2.arquitetura.adminmodule.model.SecurityFeature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityFeatureRepository extends JpaRepository<SecurityFeature, Long> {
    SecurityFeature findByModuleMnemonicAndMnemonic(String moduleMnemonic, String mnemonic);
}
