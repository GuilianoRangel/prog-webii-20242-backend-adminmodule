package br.ueg.progweb2.arquitetura.adminmodule.service;

import br.ueg.progweb2.arquitetura.adminmodule.model.*;
import br.ueg.progweb2.arquitetura.adminmodule.model.enums.StatusActiveInactive;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityFeatureRepository;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityGroupRepository;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityModuleRepository;
import br.ueg.progweb2.arquitetura.adminmodule.repository.SecurityUserRepository;
import br.ueg.progweb2.arquitetura.controllers.GenericCRUDController;
import br.ueg.progweb2.arquitetura.controllers.SecuritedController;
import br.ueg.progweb2.arquitetura.controllers.enums.ISecurityRole;
import br.ueg.progweb2.arquitetura.reflection.ReflectionUtils;
import br.ueg.progweb2.arquitetura.reflection.RestControllerInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SecurityInitializeService {
    private static final Logger LOG =
            LoggerFactory.getLogger(SecurityInitializeService.class);
    @Autowired
    private SecurityGroupRepository securityGroupRepository;
    @Autowired
    private SecurityModuleRepository securityModuleRepository;
    @Autowired
    private SecurityUserRepository securityUserRepository;

    @Autowired
    private RestControllerInspector restControllerInspector;
    @Autowired
    private SecurityFeatureRepository securityFeatureRepository;

    public void initialize(){
        LOG.info("initiateDemoInstance");
        SecurityUser securityUser = getNewSecurityUser();
        securityUser = securityUserRepository.findByLogin(securityUser.getLogin());
        //Caso já tenha usuário não executa novamente.
        if(Objects.nonNull(securityUser)){
            return;
        }

        Set<SecurityModule> securityModules = this.createModulesFromControllerWithNotExits();

        SecurityGroup securityGroup = createGrupoAdmin(securityModules);

        createUsuarioAdmin(securityGroup);
    }

    private Set<SecurityModule> createModulesFromControllerWithNotExits() {
        Set<SecurityModule> securityModules = new HashSet<>();
        List<Class<?>> controllers = this.restControllerInspector.listRestControllers();
        //TODO PAREI AQUI verificar para outra forma de filtrar, pois assim está obrigando a ser erdado de generic
        controllers.stream().map(ClassUtils::getUserClass).filter(c -> ReflectionUtils.implementsInterface(c,SecuritedController.class)).forEach(controller -> {
            GenericCRUDController<?, ?, ?, ?, ?, ?, ?, ?> restControllerBean = restControllerInspector.getRestControllerBeanByClassName(controller.getSimpleName());

            String securityModuleMNemonic = restControllerBean.getSecurityModuleName().toUpperCase();
            String securityModuleLabel = restControllerBean.getSecurityModuleLabel();
            SecurityModule securityModule = securityModuleRepository.findByName(securityModuleMNemonic);
            if(Objects.isNull(securityModule)){
                securityModule = new SecurityModule();

                securityModule.setMnemonic(securityModuleMNemonic);
                securityModule.setName(securityModuleLabel);
                securityModule.setStatus(StatusActiveInactive.ACTIVE);
                securityModule = securityModuleRepository.save(securityModule);
            }

            Set<SecurityFeature> features = getCrudFeatures(restControllerBean, securityModule);

            for(SecurityFeature securityFeature : features){
                securityFeature.setModule(securityModule);
            }

            securityModule.setFeatures(features);
            securityModule = securityModuleRepository.save(securityModule);
            securityModules.add(securityModule);
        });

        return securityModules;
    }

    /**
     * retorna Funcionalidades padrão de um CRRUD
     * @return
     */
    private Set<SecurityFeature> getCrudFeatures(GenericCRUDController<?,?,?,?,?,?,?,?> controller, SecurityModule securityModule) {
        Set<SecurityFeature> securityFeatures = new HashSet<>();

        List<ISecurityRole> securityRoles = controller.getSecurityModuleFeatures();
        String securityModuleMnemonic = securityModule.getMnemonic();

        securityRoles.forEach(securityRole -> {
            String featureMnemonic = securityRole.getName();
            SecurityFeature securityFeatureDb = securityFeatureRepository.findByModuleMnemonicAndMnemonic(securityModuleMnemonic, featureMnemonic);
            if(Objects.isNull(securityFeatureDb)) {
                securityFeatureDb = new SecurityFeature();
                securityFeatureDb.setMnemonic(securityRole.getName());
                securityFeatureDb.setName(securityRole.getLabel());
                securityFeatureDb.setStatus(StatusActiveInactive.ACTIVE);
                securityFeatures.add(securityFeatureDb);
            }
        });
        return securityFeatures;
    }

    private SecurityGroup createGrupoAdmin(Set<SecurityModule> modulos) {
        SecurityGroup securityGroupAdmin = securityGroupRepository.findByName("Administradores");

        //TODO tratar para atualizar Grupos já existentes
        if(Objects.isNull(securityGroupAdmin)){
            securityGroupAdmin = new SecurityGroup();
            securityGroupAdmin.setName("Administradores");
            securityGroupAdmin.setDescription("Grupo de Administradores");
            securityGroupAdmin.setStatus(StatusActiveInactive.ACTIVE);
            securityGroupAdmin = securityGroupRepository.save(securityGroupAdmin);

            final SecurityGroup lSecurityGroup = securityGroupAdmin;
            modulos.forEach(modulo -> {
                if(Objects.isNull(lSecurityGroup.getGroupFeatures())){
                    lSecurityGroup.setGroupFeatures(new HashSet<>());
                }
                lSecurityGroup.getGroupFeatures().addAll(
                        modulo.getFeatures().stream().map(
                                funcionalidade -> new SecurityGroupFeature(null, lSecurityGroup, funcionalidade)
                        ).collect(Collectors.toSet())
                );
            });
            securityGroupRepository.save(securityGroupAdmin);
        }

        return securityGroupAdmin;
    }
    private void createUsuarioAdmin(SecurityGroup securityGroup) {
        SecurityUser securityUser = getNewSecurityUser();

        securityUser = securityUserRepository.save(securityUser);

        Set<SecurityUserGroup> securityUserGroups = new HashSet<>();
        securityUserGroups.add(new SecurityUserGroup(null, securityUser, securityGroup));
        securityUser.setGroups(securityUserGroups);
        securityUser = securityUserRepository.save(securityUser);
    }

    private static SecurityUser getNewSecurityUser() {
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
