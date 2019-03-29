package io.scadatom.electron.service.operation;

import io.scadatom.neutron.ParticleDTO;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ParticleOperation {
  private final ParticleDTO particleDTO;
  private final DecimalFormat decimalFormat;

  public ParticleOperation(ParticleDTO particleDTO) {
    this.particleDTO = particleDTO;
    decimalFormat =
        new DecimalFormat(
            this.particleDTO.getDecimalFormat() == null
                ? "0"
                : this.particleDTO.getDecimalFormat());
    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
  }

  public ParticleDTO getParticleDTO() {
    return particleDTO;
  }

  public DecimalFormat getDecimalFormat() {
    return decimalFormat;
  }
}
