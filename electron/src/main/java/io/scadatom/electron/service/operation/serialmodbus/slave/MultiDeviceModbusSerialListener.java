package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.ModbusIOException;
import com.ghgande.j2mod.modbus.io.AbstractModbusTransport;
import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.net.AbstractModbusListener;
import com.ghgande.j2mod.modbus.net.ModbusSerialListener;
import com.ghgande.j2mod.modbus.procimg.ProcessImage;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiDeviceModbusSerialListener extends ModbusSerialListener {

    private final Logger log = LoggerFactory.getLogger(MultiDeviceModbusSerialListener.class);

    private final int respDelay;
    private final Map<Integer, ProcessImage> spiMap;

    MultiDeviceModbusSerialListener(
        SerialParameters params, int respDelay, Map<Integer, ProcessImage> spiMap) {
        super(params);
        this.respDelay = respDelay;
        this.spiMap = spiMap;
    }

    @Override
    protected void handleRequest(AbstractModbusTransport transport, AbstractModbusListener listener)
        throws ModbusIOException {
        try {
            ModbusRequest request = transport.readRequest(listener);
            if (request != null) {
                ModbusResponse response;
                ProcessImage spi = getProcessImage(request.getUnitID());
                if (spi == null) {
                    response = request.createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
                } else {
                    response = request.createResponse(this);
                    if (respDelay > 0) {
                        try {
                            Thread.sleep(respDelay);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                transport.writeMessage(response); // Write the response
                log.trace("req [{}], resp [{}]", request.getHexMessage(), response.getHexMessage());
            }  // skip invalid request

        } catch (NullPointerException e) {
            log.error("undefined port problem");
            throw e;
        }
    }

    @Override
    public ProcessImage getProcessImage(int unitId) {
        return spiMap.get(unitId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MultiDeviceModbusSerialListener{");
        sb.append("respDelay=").append(respDelay);
        sb.append(", port=").append(port);
        sb.append(", listening=").append(listening);
        sb.append(", address=").append(address);
        sb.append(", error='").append(error).append('\'');
        sb.append(", timeout=").append(timeout);
        sb.append('}');
        return sb.toString();
    }
}
