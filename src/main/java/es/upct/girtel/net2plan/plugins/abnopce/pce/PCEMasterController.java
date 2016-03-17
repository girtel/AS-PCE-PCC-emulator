/*
 * Copyright (c) $year Jose-Juan Pedreno-Manresa Jose-Luis Izquierdo-Zaragoza Pablo Pavon-Marino, .
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the GNU Lesser Public License v3
 *  which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 *  Contributors:
 */

package es.upct.girtel.net2plan.plugins.abnopce.pce;


import es.upct.girtel.net2plan.plugins.abnopce.pce.bgp.BGPHandler;
import es.upct.girtel.net2plan.plugins.abnopce.pce.pcep.PCEPHandler;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PCEMasterController
{
	private static final PCEMasterController INSTANCE = new PCEMasterController();
	private final Map<InetAddress, IPCEEntity> _entityMap;

	private PCEMasterController()
	{
		_entityMap = new HashMap<InetAddress, IPCEEntity>();
	}
	
	public Set<InetAddress> getActiveSessions()
	{
		return _entityMap.keySet();
	}

	public static PCEMasterController getInstance(){ return INSTANCE; }

	public boolean registerPCEP(PCEPHandler pcepHandler, InetAddress ip)
	{
		IPCEEntity entity = _entityMap.get(ip);
		if (entity != null)
		{
			return false;
		}
		else
		{
			entity = new BasicPCEPBGPLSSpeaker();
			entity.isPCEPRegistered = true;
			entity.pcepHandler = pcepHandler;
			_entityMap.put(ip, entity);
			return true;
		}
	}

	public boolean registerBGP(BGPHandler bgpHandler, InetAddress ip)
	{
		IPCEEntity entity = _entityMap.get(ip);

		if (entity == null)
		{
			return false;
		}
		else
		{
			entity.isBGPRegistered = true;
			entity.bgpHandler = bgpHandler;
			return true;
		}
	}
	
	public IPCEEntity getPCEEntity(InetAddress ip)
	{
		IPCEEntity entity = _entityMap.get(ip);
		if (!entity.isValid()) return null;
		
		return entity;
	}
}