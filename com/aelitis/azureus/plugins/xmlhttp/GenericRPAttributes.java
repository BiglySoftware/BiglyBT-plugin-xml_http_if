/**
 * Created on 10-Jan-2006
 * Created by Allan Crooks
 * Copyright (C) 2006 Aelitis, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * AELITIS, SAS au capital de 46,603.30 euros
 * 8 Allee Lenotre, La Grille Royale, 78600 Le Mesnil le Roi, France.
 *
 */
package com.aelitis.azureus.plugins.xmlhttp;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.biglybt.core.util.*;

import com.biglybt.pif.disk.DiskManagerFileInfo;
import com.biglybt.pif.download.*;
import com.biglybt.pif.ipfilter.IPFilter;
import com.biglybt.pif.ipfilter.IPRange;
import com.biglybt.pif.logging.LoggerChannel;
import com.biglybt.pif.peers.Peer;
import com.biglybt.pif.peers.PeerStats;
import com.biglybt.pif.PluginConfig;
import com.biglybt.pif.PluginInterface;
import com.biglybt.pif.torrent.Torrent;
import com.biglybt.pif.tracker.TrackerTorrent;
import com.biglybt.pif.utils.search.SearchException;
import com.biglybt.pif.utils.search.SearchProvider;
import com.biglybt.pif.utils.search.SearchProviderResults;
import com.biglybt.pif.utils.search.SearchResult;

import com.biglybt.pifimpl.remote.RPObject;
import  com.biglybt.pifimpl.remote.RPPluginInterface;

public class GenericRPAttributes {

    public static Map<String,Object> getAttributes(Object object, Class<?> obj_class, Map<String,Class<?>> attribute_types) {
        RemoteClassMap<String,Object> map = new RemoteClassMap<String,Object>(attribute_types);
        if (obj_class == DiskManagerFileInfo.class) {
            DiskManagerFileInfo dmfi = (DiskManagerFileInfo)object;
            map.put("access_mode",        dmfi.getAccessMode());
            map.put("downloaded",         dmfi.getDownloaded());
            map.put("file",               dmfi.getFile());
            map.put("first_piece_number", dmfi.getFirstPieceNumber());
            map.put("num_pieces",         dmfi.getNumPieces());
            map.put("is_priority",        dmfi.isPriority());
            map.put("is_skipped",         dmfi.isSkipped());
            map.put("length",             dmfi.getLength());
            map.put("link",               dmfi.getLink());            
            map.put("is_deleted",         dmfi.isDeleted());
        }
        else if (obj_class == DownloadManager.class) {
            DownloadManager dm = (DownloadManager)object;
            map.put("can_pause_downloads",  dm.canPauseDownloads());
            map.put("can_resume_downloads", dm.canResumeDownloads());
            map.put("is_seeding_only"     , dm.isSeedingOnly());
        }
        else if (obj_class == Download.class) {
            Download dload = (Download)object;
            map.put("torrent",             dload.getTorrent());
            map.put("stats",               dload.getStats());
            map.put("announce_result",     dload.getLastAnnounceResult());
            map.put("scrape_result",       dload.getLastScrapeResult());
            map.put("position",            dload.getPosition());
            map.put("force_start",         dload.isForceStart());
            map.put("name",                dload.getName());
            map.put("creation_time",       dload.getCreationTime());
            map.put("download_peer_id",    dload.getDownloadPeerId());
            map.put("error_state_details", dload.getErrorStateDetails());
            map.put("max_download_rate",   dload.getMaximumDownloadKBPerSecond());
            map.put("max_upload_rate",     dload.getUploadRateLimitBytesPerSecond());
            map.put("position",            dload.getPosition());
            map.put("save_path",           dload.getSavePath());
            map.put("seeding_rank",        dload.getSeedingRank().getRank());
            map.put("state",               dload.getState());
            map.put("sub_state",           dload.getSubState());
            map.put("torrent_file",        dload.getTorrentFileName());
            map.put("checking",            dload.isChecking());
            map.put("complete",            dload.isComplete());
            map.put("messaging_enabled",   dload.isMessagingEnabled());
            map.put("paused",              dload.isPaused());
            map.put("persistent",          dload.isPersistent());
            map.put("flags",         	   dload.getFlags());
    	}
        else if (obj_class == DownloadAnnounceResult.class) {
            DownloadAnnounceResult dar = (DownloadAnnounceResult)object;
            map.put("seed_count",          dar.getSeedCount());
            map.put("non_seed_count",      dar.getNonSeedCount());
            map.put("error",               dar.getError());
            map.put("reported_peer_count", dar.getReportedPeerCount());
            map.put("response_type",       dar.getResponseType());
            map.put("time_to_wait",        dar.getTimeToWait());
            map.put("url",                 dar.getURL());
        }
        else if (obj_class == DownloadScrapeResult.class) {
            DownloadScrapeResult dsr = (DownloadScrapeResult)object;
            map.put("seed_count",      dsr.getSeedCount());
            map.put("non_seed_count",  dsr.getNonSeedCount());
            map.put("next_start_time", dsr.getNextScrapeStartTime());
            map.put("response_type",   dsr.getResponseType());
            map.put("start_time",      dsr.getScrapeStartTime());
            map.put("status",          dsr.getStatus());
            map.put("url",             dsr.getURL());
        }
        else if (obj_class == DownloadStats.class) {
            DownloadStats stats = (DownloadStats)object;
            map.put("downloaded",              stats.getDownloaded());
            map.put("uploaded",                stats.getUploaded());
            map.put("completed",               stats.getCompleted());
            map.put("downloadCompletedLive",   stats.getDownloadCompleted(true));
            map.put("downloadCompletedStored", stats.getDownloadCompleted(false));
            map.put("status",                  stats.getStatus());
            map.put("status_localised",        stats.getStatus(true));
            map.put("upload_average",          stats.getUploadAverage());
            map.put("download_average",        stats.getDownloadAverage());
            map.put("eta",                     stats.getETA());
            map.put("share_ratio",             stats.getShareRatio());
            map.put("availability",            stats.getAvailability());
            map.put("health",                  stats.getHealth());
            map.put("discarded",               stats.getDiscarded());
            map.put("elapsed_time",            stats.getElapsedTime());
            map.put("hash_fails",              stats.getHashFails());
            map.put("seconds_downloading",     stats.getSecondsDownloading());
            map.put("seconds_only_seeding",    stats.getSecondsOnlySeeding());
            map.put("download_directory",      stats.getDownloadDirectory());
            map.put("target_file_or_dir",      stats.getTargetFileOrDir());
            map.put("time_started",            stats.getTimeStarted());
            map.put("time_started_seeding",    stats.getTimeStartedSeeding());
            map.put("total_average",           stats.getTotalAverage());
            map.put("tracker_status",          stats.getTrackerStatus());
            map.put("remaining",               stats.getRemaining());
        }
        else if (obj_class == IPFilter.class) {
            IPFilter filter = (IPFilter)object;
            map.put("last_update_time",      filter.getLastUpdateTime());
            map.put("number_of_ranges",      filter.getNumberOfRanges());
            map.put("number_of_blocked_ips", filter.getNumberOfBlockedIPs());
        }
        else if (obj_class == IPRange.class) {
            IPRange range = (IPRange)object;
            map.put("description", range.getDescription());
            map.put("start_ip",    range.getStartIP());
            map.put("end_ip",      range.getEndIP());
        }
        else if (obj_class == LoggerChannel.class) {
        	LoggerChannel lc = (LoggerChannel)object;
        	map.put("name",    lc.getName());
        	map.put("enabled", lc.isEnabled());
        }
        else if (obj_class == Peer.class) {
            Peer peer = (Peer)object;
            map.put("stats",              peer.getStats());
            map.put("ip",                 peer.getIp());
            map.put("port",               peer.getPort());
            map.put("client",             peer.getClient());
            map.put("id",                 peer.getId());
            map.put("percent_done",       peer.getPercentDoneInThousandNotation());
            map.put("snubbed_time",       peer.getSnubbedTime());
            map.put("state",              peer.getState());
            map.put("choked",             peer.isChoked());
            map.put("choking",            peer.isChoking());
            map.put("download_possible",  peer.isDownloadPossible());
            map.put("incoming",           peer.isIncoming());
            map.put("interested",         peer.isInterested());
            map.put("interesting",        peer.isInteresting());
            map.put("optimistic_unchoke", peer.isOptimisticUnchoke());
            map.put("seed",               peer.isSeed());
            map.put("snubbed",            peer.isSnubbed());
            map.put("transfer_available", peer.isTransferAvailable());
        }
        else if (obj_class == PeerStats.class) {
            PeerStats stats = (PeerStats)object;
            map.put("download_average",                  stats.getDownloadAverage());
            map.put("reception",                         stats.getReception());
            map.put("statistic_sent_average",            stats.getStatisticSentAverage());
            map.put("time_since_connection_established", stats.getTimeSinceConnectionEstablished());
            map.put("total_average",                     stats.getTotalAverage());
            map.put("total_discarded",                   stats.getTotalDiscarded());
            map.put("total_received",                    stats.getTotalReceived());
            map.put("total_sent",                        stats.getTotalSent());
            map.put("upload_average",                    stats.getUploadAverage());
         }
        else if (obj_class == PluginConfig.class) {
            PluginConfig pconfig = (PluginConfig)object;
            String[] int_property_names = new String[] {
                PluginConfig.CORE_PARAM_INT_MAX_UPLOAD_SPEED_KBYTES_PER_SEC,
                PluginConfig.CORE_PARAM_INT_MAX_UPLOAD_SPEED_SEEDING_KBYTES_PER_SEC,
                PluginConfig.CORE_PARAM_INT_MAX_DOWNLOAD_SPEED_KBYTES_PER_SEC,
                PluginConfig.CORE_PARAM_INT_MAX_CONNECTIONS_PER_TORRENT,
                PluginConfig.CORE_PARAM_INT_MAX_CONNECTIONS_GLOBAL,
                PluginConfig.CORE_PARAM_INT_MAX_DOWNLOADS,
                PluginConfig.CORE_PARAM_INT_MAX_ACTIVE,
                PluginConfig.CORE_PARAM_INT_MAX_ACTIVE_SEEDING,
                PluginConfig.CORE_PARAM_INT_MAX_UPLOADS,
                PluginConfig.CORE_PARAM_INT_MAX_UPLOADS_SEEDING
            };
           
            int	num_ints 	= int_property_names.length;
            
            Integer[] int_property_values = new Integer[num_ints];
            for (int i=0; i<num_ints; i++) {
            	int_property_values[i] = pconfig.getCoreIntParameter(int_property_names[i]);
            }
            
            String[] boolean_property_names = new String[] {
                    PluginConfig.CORE_PARAM_BOOLEAN_AUTO_SPEED_ON,
                    PluginConfig.CORE_PARAM_BOOLEAN_AUTO_SPEED_SEEDING_ON,
                    PluginConfig.CORE_PARAM_BOOLEAN_MAX_UPLOAD_SPEED_SEEDING,
                    PluginConfig.CORE_PARAM_BOOLEAN_MAX_ACTIVE_SEEDING,
                };
            int	num_bools 	= boolean_property_names.length;

            Boolean[] boolean_property_values = new Boolean[num_bools];
            for (int i=0; i<num_bools; i++) {
            	boolean_property_values[i] = pconfig.getCoreBooleanParameter(boolean_property_names[i]);
            }
            
            String[]	property_names = new String[num_ints + num_bools ];
                               
            System.arraycopy(int_property_names, 0, property_names, 0, num_ints );
            System.arraycopy(boolean_property_names, 0, property_names, num_ints, num_bools );
                  
            Object[]	property_values = new Object[num_ints + num_bools ];
            
            System.arraycopy(int_property_values, 0, property_values, 0, num_ints );
            System.arraycopy(boolean_property_values, 0, property_values, num_ints, num_bools );
 
            map.put("cached_property_names",  property_names);
            map.put("cached_property_values", property_values);
        }
        else if (obj_class == PluginInterface.class) {
            PluginInterface pi = (PluginInterface)object;
            map.put("azureus_name",    Constants.APP_NAME);
            map.put("azureus_version", Constants.getCurrentVersion());
            map.put("plugin_id",       pi.getPluginID());
            map.put("plugin_name",     pi.getPluginName());
        }
        else if (obj_class == Torrent.class) {
            Torrent torrent = (Torrent)object;
            map.put("name",          torrent.getName());
            map.put("size",          torrent.getSize());
            map.put("hash",          torrent.getHash());
            map.put("comment",       torrent.getComment());
            map.put("created_by",    torrent.getCreatedBy());
            map.put("creation_date", torrent.getCreationDate());
            map.put("encoding",      torrent.getEncoding());
            map.put("piece_count",   torrent.getPieceCount());
            map.put("piece_size",    torrent.getPieceSize());
            map.put("private",       torrent.isPrivate());
            map.put("announce_url",  torrent.getAnnounceURL());
        }
        else if (obj_class == TrackerTorrent.class) {
            TrackerTorrent ttobject = (TrackerTorrent)object;
            map.put("torrent",                ttobject.getTorrent());
            map.put("status",                 ttobject.getStatus());
            map.put("total_uploaded",         ttobject.getTotalUploaded());
            map.put("total_downloaded",       ttobject.getTotalDownloaded());
            map.put("average_uploaded",       ttobject.getAverageUploaded());
            map.put("average_downloaded",     ttobject.getAverageDownloaded());
            map.put("total_left",             ttobject.getTotalLeft());
            map.put("completed_count",        ttobject.getCompletedCount());
            map.put("total_bytes_in",         ttobject.getTotalBytesIn());
            map.put("average_bytes_in",       ttobject.getAverageBytesIn());
            map.put("total_bytes_out",        ttobject.getTotalBytesOut());
            map.put("average_bytes_out",      ttobject.getAverageBytesOut());
            map.put("scrape_count",           ttobject.getScrapeCount());
            map.put("average_scrape_count",   ttobject.getAverageScrapeCount());
            map.put("announce_count",         ttobject.getAnnounceCount());
            map.put("average_announce_count", ttobject.getAverageAnnounceCount());
            map.put("seed_count",             ttobject.getSeedCount());
            map.put("leecher_count",          ttobject.getLeecherCount());
            map.put("bad_NAT_count",          ttobject.getBadNATCount());
        }
        else if (obj_class == SearchProvider.class) {
        	SearchProvider spobject = (SearchProvider)object;
            map.put("id",           ((Long)spobject.getProperty(SearchProvider.PR_ID)).longValue());
            map.put("name",         spobject.getProperty(SearchProvider.PR_NAME));
        }
        else if (obj_class == SearchProviderResults.class) {
        	SearchProviderResults sprobject = (SearchProviderResults)object;
            map.put("provider_id", ((Long)sprobject.getProvider().getProperty(SearchProvider.PR_ID)).longValue());
            map.put("results",		sprobject.getResults());
            map.put("complete",		sprobject.isComplete());
            SearchException error = sprobject.getError();
            map.put("error",		error==null?null:Debug.getNestedExceptionMessage(error));
        }        
        else if (obj_class == SearchResult.class) {
        	SearchResult robject = (SearchResult)object;
        	map.put("name",          	robject.getProperty( SearchResult.PR_NAME ));
        	Date pub_date = (Date)robject.getProperty( SearchResult.PR_PUB_DATE );
        	map.put("pub_date",      	pub_date==null?null:TimeFormatter.getHTTPDate(pub_date.getTime()));
        	map.put("size",          	robject.getProperty( SearchResult.PR_SIZE ));
        	map.put("leechers",      	robject.getProperty( SearchResult.PR_LEECHER_COUNT ));
        	map.put("seeds",         	robject.getProperty( SearchResult.PR_SEED_COUNT ));
        	map.put("super_seeds",   	robject.getProperty( SearchResult.PR_SUPER_SEED_COUNT ));
        	map.put("category",      	robject.getProperty( SearchResult.PR_CATEGORY ));
        	map.put("comments",      	robject.getProperty( SearchResult.PR_COMMENTS ));
        	map.put("votes",         	robject.getProperty( SearchResult.PR_VOTES ));
        	map.put("content_type",  	robject.getProperty( SearchResult.PR_CONTENT_TYPE ));
        	map.put("details_link",     robject.getProperty( SearchResult.PR_DETAILS_LINK ));
        	map.put("download_link",    robject.getProperty( SearchResult.PR_DOWNLOAD_LINK ));
        	map.put("play_link",        robject.getProperty( SearchResult.PR_PLAY_LINK ));
        	map.put("private",         	robject.getProperty( SearchResult.PR_PRIVATE ));
        	map.put("drm_key",          robject.getProperty( SearchResult.PR_DRM_KEY ));
        	map.put("download_b_link",  robject.getProperty( SearchResult.PR_DOWNLOAD_BUTTON_LINK ));
        	map.put("rank",          	robject.getProperty( SearchResult.PR_RANK ));
        	map.put("accuracy",        	robject.getProperty( SearchResult.PR_ACCURACY ));
        	map.put("votes_down",      	robject.getProperty( SearchResult.PR_VOTES_DOWN ));
        	map.put("uid",          	robject.getProperty( SearchResult.PR_UID ));
        	map.put("hash",          	robject.getProperty( SearchResult.PR_HASH )); 
        }
       
        
        
        
        return map;
    }

    private static Map<Class<?>,Map<String,Class<?>>> class_definitions = null;
    static {
        class_definitions = new HashMap<Class<?>,Map<String,Class<?>>>();
        Map<String,Class<?>> attributes = null;
        Class<?> plugin_class = null;

        attributes = new HashMap<String,Class<?>>();
        plugin_class = DiskManagerFileInfo.class;
        attributes.put("access_mode",        int.class);
        attributes.put("downloaded",         long.class);
        attributes.put("file",               File.class);
        attributes.put("first_piece_number", int.class);
        attributes.put("num_pieces",         int.class);
        attributes.put("is_priority",        boolean.class);
        attributes.put("is_skipped",         boolean.class);
        attributes.put("length",             long.class);
        attributes.put("link",               File.class);        
        attributes.put("is_deleted",         boolean.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = DownloadManager.class;
        attributes.put("can_pause_downloads",  boolean.class);
        attributes.put("can_resume_downloads", boolean.class);
        attributes.put("is_seeding_only",      boolean.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = Download.class;
        attributes.put("torrent",             Torrent.class);
        attributes.put("stats",               DownloadStats.class);
        attributes.put("announce_result",     DownloadAnnounceResult.class);
        attributes.put("scrape_result",       DownloadScrapeResult.class);
        attributes.put("position",            int.class);
        attributes.put("force_start",         boolean.class);
        attributes.put("name",                String.class);
        attributes.put("creation_time",       long.class);
        attributes.put("download_peer_id",    byte[].class);
        attributes.put("error_state_details", String.class);
        attributes.put("max_download_rate",   int.class);
        attributes.put("max_upload_rate",     int.class);
        attributes.put("position",            int.class);
        attributes.put("save_path",           String.class);
        attributes.put("seeding_rank",        int.class);
        attributes.put("state",               int.class);
        attributes.put("sub_state",           int.class);
        attributes.put("torrent_file",        String.class);
        attributes.put("checking",            boolean.class);
        attributes.put("complete",            boolean.class);
        attributes.put("messaging_enabled",   boolean.class);
        attributes.put("paused",              boolean.class);
        attributes.put("persistent",          boolean.class);
        attributes.put("flags",        		  long.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = DownloadAnnounceResult.class;
        attributes.put("seed_count",          int.class);
        attributes.put("non_seed_count",      int.class);
        attributes.put("error",               String.class);
        attributes.put("reported_peer_count", int.class);
        attributes.put("response_type",       int.class);
        attributes.put("time_to_wait",        long.class);
        attributes.put("url",                 URL.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = DownloadScrapeResult.class;
        attributes.put("seed_count",      int.class);
        attributes.put("non_seed_count",  int.class);
        attributes.put("next_start_time", long.class);
        attributes.put("response_type",   int.class);
        attributes.put("start_time",      long.class);
        attributes.put("status",          String.class);
        attributes.put("url",             URL.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = DownloadStats.class;
        attributes.put("downloaded",              long.class);
        attributes.put("uploaded",                long.class);
        attributes.put("completed",               int.class);
        attributes.put("downloadCompletedLive",   int.class);
        attributes.put("downloadCompletedStored", int.class);
        attributes.put("status",                  String.class);
        attributes.put("status_localised",        String.class);
        attributes.put("upload_average",          long.class);
        attributes.put("download_average",        long.class);
        attributes.put("eta",                     String.class);
        attributes.put("share_ratio",             int.class);
        attributes.put("availability",            float.class);
        attributes.put("health",                  int.class);
        attributes.put("discarded",               long.class);
        attributes.put("elapsed_time",            String.class);
        attributes.put("hash_fails",              long.class);
        attributes.put("seconds_downloading",     long.class);
        attributes.put("seconds_only_seeding",    long.class);
        attributes.put("download_directory",      String.class);
        attributes.put("target_file_or_dir",      String.class);
        attributes.put("time_started",            long.class);
        attributes.put("time_started_seeding",    long.class);
        attributes.put("total_average",           long.class);
        attributes.put("tracker_status",          String.class);
        attributes.put("remaining",               long.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = IPFilter.class;
        attributes.put("last_update_time",      long.class);
        attributes.put("number_of_ranges",      int.class);
        attributes.put("number_of_blocked_ips", int.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = IPRange.class;
        attributes.put("description", String.class);
        attributes.put("start_ip",    String.class);
        attributes.put("end_ip",      String.class);
        class_definitions.put(plugin_class, attributes);
        
        attributes = new HashMap<String,Class<?>>();
        plugin_class = LoggerChannel.class;
        attributes.put("enabled", boolean.class);
        attributes.put("name",    String.class);
        class_definitions.put(plugin_class, attributes);
        
        attributes = new HashMap<String,Class<?>>();
        plugin_class = Peer.class;
        attributes.put("stats",              PeerStats.class);
        attributes.put("ip"  ,               String.class);
        attributes.put("port",               int.class);
        attributes.put("client",             String.class);
        attributes.put("id",                 byte[].class);
        attributes.put("percent_done",       int.class);
        attributes.put("snubbed_time",       long.class);
        attributes.put("state",              int.class);
        attributes.put("choked",             boolean.class);
        attributes.put("choking",            boolean.class);
        attributes.put("download_possible",  boolean.class);
        attributes.put("incoming",           boolean.class);
        attributes.put("interested",         boolean.class);
        attributes.put("interesting",        boolean.class);
        attributes.put("optimistic_unchoke", boolean.class);
        attributes.put("seed",               boolean.class);
        attributes.put("snubbed",            boolean.class);
        attributes.put("transfer_available", boolean.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = PeerStats.class;
        attributes.put("download_average",                  int.class);
        attributes.put("reception",                         int.class);
        attributes.put("statistic_sent_average",            int.class);
        attributes.put("time_since_connection_established", long.class);
        attributes.put("total_average",                     int.class);
        attributes.put("total_discarded",                   long.class);
        attributes.put("total_received",                    long.class);
        attributes.put("total_sent",                        long.class);
        attributes.put("upload_average",                    int.class);
        class_definitions.put(plugin_class, attributes);
        
        attributes = new HashMap<String,Class<?>>();
        plugin_class = PluginConfig.class;
        attributes.put("cached_property_names",  new String[0].getClass());
        attributes.put("cached_property_values", new Object[0].getClass());
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = PluginInterface.class;
        attributes.put("azureus_name",    String.class);
        attributes.put("azureus_version", String.class);
        attributes.put("plugin_id",       String.class);
        attributes.put("plugin_name",     String.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = Torrent.class;
        attributes.put("name",          String.class);
        attributes.put("size",          long.class);
        attributes.put("hash",          new byte[0].getClass());
        attributes.put("comment",       String.class);
        attributes.put("created_by",    String.class);
        attributes.put("creation_date", long.class);
        attributes.put("encoding",      String.class);
        attributes.put("piece_count",   long.class);
        attributes.put("piece_size",    long.class);
        attributes.put("private",       boolean.class);
        attributes.put("announce_url",  URL.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = TrackerTorrent.class;
        attributes.put("torrent",                Torrent.class);
        attributes.put("status",                 int.class);
        attributes.put("total_uploaded",         long.class);
        attributes.put("total_downloaded",       long.class);
        attributes.put("average_uploaded",       long.class);
        attributes.put("average_downloaded",     long.class);
        attributes.put("total_left",             long.class);
        attributes.put("completed_count",        long.class);
        attributes.put("total_bytes_in",         long.class);
        attributes.put("average_bytes_in",       long.class);
        attributes.put("total_bytes_out",        long.class);
        attributes.put("average_bytes_out",      long.class);
        attributes.put("scrape_count",           long.class);
        attributes.put("average_scrape_count",   long.class);
        attributes.put("announce_count",         long.class);
        attributes.put("average_announce_count", long.class);
        attributes.put("seed_count",             int.class);
        attributes.put("leecher_count",          int.class);
        attributes.put("bad_NAT_count",          int.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = SearchProvider.class;
        attributes.put("id",                	 long.class);
        attributes.put("name",                   String.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = SearchProviderResults.class;
        attributes.put("provider_id",          	 long.class);
        attributes.put("results",                SearchResult[].class);
        attributes.put("complete",               boolean.class);
        attributes.put("error",              	 String.class);
        class_definitions.put(plugin_class, attributes);
                
        attributes = new HashMap<String,Class<?>>();
        plugin_class = SearchResult.class;
        attributes.put("name",          	String.class);
        attributes.put("pub_date",         	String.class);
        attributes.put("size",          	long.class);
        attributes.put("leechers",         	long.class);
        attributes.put("seeds",          	long.class);
        attributes.put("super_seeds",      	long.class);
        attributes.put("category",         	String.class);
        attributes.put("comments",         	long.class);
        attributes.put("votes",          	long.class);
        attributes.put("content_type",     	String.class);
        attributes.put("details_link",     	String.class);
        attributes.put("download_link",    	String.class);
        attributes.put("play_link",        	String.class);
        attributes.put("private",          	boolean.class);
        attributes.put("drm_key",          	String.class);
        attributes.put("download_b_link",  	String.class);
        attributes.put("rank",          	long.class);
        attributes.put("accuracy",         	long.class);
        attributes.put("votes_down",        long.class);
        attributes.put("uid",          	 	String.class);
        attributes.put("hash",          	byte[].class);    
        class_definitions.put(plugin_class, attributes);

         
        
        
        attributes = new HashMap<String,Class<?>>();
        plugin_class = RPObject.class;
        attributes.put("_object_id", int.class);
        class_definitions.put(plugin_class, attributes);

        attributes = new HashMap<String,Class<?>>();
        plugin_class = RPPluginInterface.class;
        attributes.put("_object_id",     long.class);
        attributes.put("_connection_id", long.class);
        class_definitions.put(plugin_class, attributes);
    }

    public static Map<String,Class<?>> getAttributeTypes(Class<?> c) {
    	Map<String,Class<?>> result = class_definitions.get(c);
        if (result == null) {
            result = (Map<String,Class<?>>)Collections.EMPTY_MAP;
        }
        return result;
    }

    public static Map<String,Class<?>>  getRPAttributeTypes(Class<?> c) {
        Map<String,Class<?>> result = null;
        if (RPPluginInterface.class.isAssignableFrom(c)) {
            result = class_definitions.get(RPPluginInterface.class);
        }
        else /* (RPObject.class.isAssignableFrom(c)) */ {
            result = class_definitions.get(RPObject.class);
        }
        return result;
    }

    public static Map<String,Object> getRPAttributes(RPObject object, Class<?> obj_class, Map<String,Class<?>> attribute_types) {
        RemoteClassMap<String,Object> map = new RemoteClassMap<String,Object>(attribute_types);
        if (RPPluginInterface.class.isAssignableFrom(obj_class)) {
            map.put("_connection_id", ((GenericRPPluginInterface)object)._connection_id);
        }
        map.put("_object_id", object._getOID());
        return map;
    }


    private static class RemoteClassMap<S,T> extends HashMap<String,Object> {

        private static RemoteMethodInvoker invoker = RemoteMethodInvoker.create(null);

        private Map<String,Class<?>> attribute_types;

        public RemoteClassMap(Map<String,Class<?>> attribute_types) {
            super();
            this.attribute_types = attribute_types;
        }

        public Object put(String key, Object value) {
            try {
                Class<?> c_type = attribute_types.get(key);
                if (c_type == null) {
                    throw new RuntimeException("error - missing type definition for " + key + " in RemoteClassMap");
                }
                return super.put(key, invoker.prepareRemoteResult(value, c_type));
            }
            catch (java.lang.reflect.InvocationTargetException ite) {
                throw new RuntimeException(ite);
            }
            catch (NoSuchMethodException nsme) {
                throw new RuntimeException(nsme);
            }
        }

        public Object put(String key, byte[] value) {
            return super.put(key, value);
        }

        public Object put(String key, boolean value) {
            return super.put(key, Boolean.valueOf(value));
        }

        public Object put(String key, float value) {
            return super.put(key, new Float(value));
        }

        public Object put(String key, int value) {
            return super.put(key, new Integer(value));
        }

        public Object put(String key, long value) {
            return super.put(key, new Long(value));
        }

        public Object put(String key, File value) {
            return super.put(key, value);
        }

        public Object put(String key, URL value) {
            return super.put(key, value);
        }


        public Object put(String key, String value) {
            return super.put(key, value);
        }

        public Object put(String key, String[] value) {
            return super.put(key, value);
        }

        public Object put(String key, int[] value) {
            return super.put(key, value);
        }

    }

}