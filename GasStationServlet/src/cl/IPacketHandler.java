package cl;

import pl.BasePacket.PacketsOpcodes;

public interface IPacketHandler {
	void HandlePacket(ClientEntity sender, PacketsOpcodes opcode, Object data);
}
