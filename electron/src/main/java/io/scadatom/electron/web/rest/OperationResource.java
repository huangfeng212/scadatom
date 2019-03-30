package io.scadatom.electron.web.rest;

import io.scadatom.electron.service.operation.OperationService;
import io.scadatom.neutron.ElectronOpDTO;
import io.scadatom.neutron.OpCtrlReq;
import io.scadatom.neutron.OpException;
import io.scadatom.neutron.ParticleOpDTO;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** OperationResource controller */
@RestController
@RequestMapping("/api/operation")
public class OperationResource {

  private final Logger log = LoggerFactory.getLogger(OperationResource.class);
  private final OperationService operationService;

  public OperationResource(OperationService operationService) {
    this.operationService = operationService;
  }

  /** GET viewElectron */
  @GetMapping("/electron/{id}")
  public ElectronOpDTO viewElectron(@PathVariable Long id) throws OpException {
    return operationService.viewElectron(id);
  }

  /** PUT ctrlElectron */
  @PutMapping("/electron")
  public void ctrlElectron(@Valid @RequestBody OpCtrlReq opCtrlReq) throws OpException {
    opCtrlReq.setUser(SecurityContextHolder.getContext().getAuthentication().getName());
    operationService.ctrlElectron(opCtrlReq);
  }

  /** GET viewParticle */
  @GetMapping("/particle/{id}")
  public ParticleOpDTO viewParticle(@PathVariable Long id) throws OpException {
    return operationService.viewParticle(id);
  }

  /** PUT ctrlParticle */
  @PutMapping("/particle")
  public void ctrlParticle(@Valid @RequestBody OpCtrlReq opCtrlReq) throws OpException {
    opCtrlReq.setUser(SecurityContextHolder.getContext().getAuthentication().getName());
    operationService.ctrlParticle(opCtrlReq, false);
  }
}
