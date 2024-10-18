package br.ueg.progweb2.arquitetura.adminmodule.service;

import br.ueg.progweb2.arquitetura.adminmodule.model.*;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityGroupRepository;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityModuleRepository;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SecurityInitializeService {
    private static final Logger LOG =
            LoggerFactory.getLogger(SecurityInitializeService.class);
    @Autowired
    private SecurityGroupRepository grupoRepository;
    @Autowired
    private SecurityModuleRepository securityModuleRepository;
    @Autowired
    private SecurityUserRepository usuarioRepository;

    public void initialize(){
        LOG.info("initiateDemoInstance");
        SecurityUser securityUser = getNewUsuario();
        securityUser = usuarioRepository.findByLogin(securityUser.getLogin());
        //Caso já tenha usuário não executa novamente.
        if(Objects.nonNull(securityUser)){
            return;
        }

        SecurityModule moduloUsuario = createModuloCrud("SECURITYUSER", "Manter Usuário");

        SecurityModule moduloGrupo = createModuloCrud("SECURITYGROUP", "Manter Grupo");

        SecurityGroup securityGroup = createGrupoAdmin(Arrays.asList(moduloUsuario, moduloGrupo));

        createUsuarioAdmin(securityGroup);
    }

    private SecurityModule createModuloCrud(String moduloMNemonico, String moduloNome) {
        SecurityModule moduloUsuario = new SecurityModule();

        moduloUsuario.setMnemonic(moduloMNemonico);
        moduloUsuario.setName(moduloNome);
        moduloUsuario.setStatus(StatusActiveInactive.ACTIVE);
        moduloUsuario = securityModuleRepository.save(moduloUsuario);

        final SecurityModule lModuloUsuario = moduloUsuario;
        Set<SecurityFeature> funcionaldiades = getFuncionalidadesCrud();

/*        funcionaldiades.stream().map(
                funcionalidade -> {
                    funcionalidade.setModulo(lModuloUsuario);
                    return funcionalidade;
                }).collect(Collectors.toSet());
        // equivalente com for*/
        for(SecurityFeature securityFeature : funcionaldiades){
            securityFeature.setModule(moduloUsuario);
        }

        moduloUsuario.setFeatures(funcionaldiades);
        moduloUsuario = securityModuleRepository.save(moduloUsuario);
        return moduloUsuario;
    }

    /**
     * retorna Funcionalidades padrão de um CRRUD
     * @return
     */
    private Set<SecurityFeature> getFuncionalidadesCrud() {
        Set<SecurityFeature> securityFeatures = new HashSet<>();

        SecurityFeature fManter = new SecurityFeature();
        fManter.setMnemonic("CREATE");
        fManter.setName("Incluir");
        fManter.setStatus(StatusActiveInactive.ACTIVE);
        securityFeatures.add(fManter);

        fManter = new SecurityFeature();
        fManter.setMnemonic("ALTER");
        fManter.setName("Alterar");
        fManter.setStatus(StatusActiveInactive.ACTIVE);
        securityFeatures.add(fManter);

        fManter = new SecurityFeature();
        fManter.setMnemonic("ACTIVATE_INACTIVATE");
        fManter.setName("Ativar/Inativar");
        fManter.setStatus(StatusActiveInactive.ACTIVE);
        securityFeatures.add(fManter);

        fManter = new SecurityFeature();
        fManter.setMnemonic("SEARCH");
        fManter.setName("Pesquisar");
        fManter.setStatus(StatusActiveInactive.ACTIVE);
        securityFeatures.add(fManter);

        fManter = new SecurityFeature();
        fManter.setMnemonic("READ_ALL");
        fManter.setName("Listar todos");
        fManter.setStatus(StatusActiveInactive.ACTIVE);
        securityFeatures.add(fManter);

        fManter = new SecurityFeature();
        fManter.setMnemonic("READ");
        fManter.setName("Visualizar");
        fManter.setStatus(StatusActiveInactive.ACTIVE);
        securityFeatures.add(fManter);
        return securityFeatures;
    }

    private SecurityGroup createGrupoAdmin(List<SecurityModule> modulos) {
        SecurityGroup securityGroup = new SecurityGroup();
        securityGroup.setName("Administradores");
        securityGroup.setDescription("Grupo de Administradores");
        securityGroup.setStatus(StatusActiveInactive.ACTIVE);
        securityGroup = grupoRepository.save(securityGroup);
        final SecurityGroup lSecurityGroup = securityGroup;
        securityGroup.setGroupFeatures(new HashSet<>());

        modulos.forEach(modulo -> {
            lSecurityGroup.getGroupFeatures().addAll(
                    modulo.getFeatures().stream().map(
                            funcionalidade -> new SecurityGroupFeature(null, lSecurityGroup, funcionalidade)
                    ).collect(Collectors.toSet())
            );
        });

        grupoRepository.save(securityGroup);
        return securityGroup;
    }
    private void createUsuarioAdmin(SecurityGroup securityGroup) {
        SecurityUser securityUser = getNewUsuario();

        securityUser = usuarioRepository.save(securityUser);

        Set<SecurityUserGroup> securityUserGroups = new HashSet<>();
        securityUserGroups.add(new SecurityUserGroup(null, securityUser, securityGroup));
        securityUser.setGroups(securityUserGroups);
        securityUser = usuarioRepository.save(securityUser);
    }

    private static SecurityUser getNewUsuario() {
        SecurityUser securityUser = new SecurityUser();
        securityUser.setStatus(StatusActiveInactive.ACTIVE);
        securityUser.setCreatedDate(LocalDate.now());
        securityUser.setLastUpdateDate(LocalDate.now());
        securityUser.setFones(new HashSet<>());
        securityUser.setLogin("admin");
        securityUser.setName("Administrador");
        securityUser.setEmail("admin@teste.com.br");
        securityUser.setPassword(new BCryptPasswordEncoder().encode("admin"));
        return securityUser;
    }

}
