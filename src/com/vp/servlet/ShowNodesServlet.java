package com.vp.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.vp.domain.Block;
import com.vp.domain.DesignStep;
import com.vp.domain.Link;
import com.vp.domain.ScsAndLinks;
import com.vp.semanticcell.SemanticCell;
import com.vp.service.DesignHistoryService;
import com.vp.service.FeatureRelationService;
import com.vp.service.SemanticAndLinkService;
import com.vp.service.VisualService;

public class ShowNodesServlet extends HttpServlet {

	private SemanticAndLinkService semanticAndLinkService = new SemanticAndLinkService();
	private VisualService visualService = new VisualService();
	private DesignHistoryService designHistoryService = new DesignHistoryService();

	private FeatureRelationService featureRelationService = new FeatureRelationService();
	private Gson gson = new Gson();

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");

		try {
			Method method = getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鑾峰彇璧峰鐨勬牴鑺傜偣
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void showBaseSemanticCells(HttpServletRequest request, HttpServletResponse response) throws IOException {

		List<SemanticCell> scs = semanticAndLinkService.getBaseSemanticCells();
		List<Link> links = new ArrayList<Link>();

		ScsAndLinks scsAndLinks = new ScsAndLinks(scs, links);

		String scsAndLinksGson = gson.toJson(scsAndLinks);

		response.getWriter().write(scsAndLinksGson);
	}

	/**
	 * 鏍规嵁InstancedId鑾峰彇璇箟鍏冭妭鐐瑰拰杩炴帴
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getScsAndLinksFromInstancedId(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String instancedId = request.getParameter("instancedId");

		List<SemanticCell> scs = semanticAndLinkService.getSemanticCellsByInstanceId(instancedId);
		List<Link> links = semanticAndLinkService.getLinksByFromId(instancedId);

		ScsAndLinks scsAndLinks = new ScsAndLinks(scs, links);
		String scsAndLinksGson = gson.toJson(scsAndLinks);

		response.getWriter().write(scsAndLinksGson);
	}

	public void handleStepFile(HttpServletRequest request, HttpServletResponse response) {
		String stepFileName = request.getParameter("stepFileName");
		String stepData = request.getParameter("stepData");
	}

	/**
	 * 鑾峰彇浠庢牴鑺傜偣鍒癮dvanced_face
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void showSCToFace(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ScsAndLinks scsAndLinks = semanticAndLinkService.getToFace();

		String scsAndLinksGson = gson.toJson(scsAndLinks);

		response.getWriter().write(scsAndLinksGson);
	}

	/**
	 * 鑾峰彇璁捐鎰忓浘璇箟鍏冭妭鐐瑰浘
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void showSemanticNodeGraph(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ScsAndLinks scsAndLinks = semanticAndLinkService.getSemanticNodesAndLinks();

		String scsAndLinksGson = gson.toJson(scsAndLinks);

		response.getWriter().write(scsAndLinksGson);
	}

	/**
	 * 鑾峰彇鎺ㄧ悊鍚庣殑璇箟鍏冨浘
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void reasonNodeGraph(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// String[] instancedIds =
		// {"#546","#548","#448","#456","#547","#545","#461","#465","#462","#463",
		// "#407","#408","#413","#414","#437","#447","#467","#470","#469","#471","#472","#481","#473","#484","#505,#525","#510","#528",
		// "#526","#530","#496","#497","#498","#499","#500","#501","#502","#503","#504",
		// "#427","#428","#429","#434","#435","#436","#450","#458","#459","#475","#476","#477","#511","#531","#532","#535","#537","#538",
		// "#421","#442","#443","#444","#445","#448","#451","#452","#453","#454",
		// "%cylindrical_convex_feature","%step_feature","%blind_hole_feature","%through_hole_feature","%convex_feature",
		// "#387","#388","#389","#390","#391","#392","#393","#394",
		// "#386","#395","#396","#397","#398","#399","#400","#401"};

		String[] instancedIds = { "#part", "#part1", "#part2", "#block1", "#block2", "#hole1", "#hole2", "#hole3",
				"#hole4", "#concave1", "#concave2", "#concave3", "#696", "#697", "#698", "#699", "#700", "#701", "#702",
				"#703", "#704", "#705", "#706", "#707", "#708", "#709", "#710", "#711", "#712", "#725", "728", "729",
				"#730", "#731", "#732", "#733", "#734", "#735", "#736", "#737", "#738", "#739", "#740", "#741", "744" };

		List<SemanticCell> scs = semanticAndLinkService.getSemanticCellsByIds(instancedIds);

		List<Link> links = semanticAndLinkService.getLinksByScs(scs);

		ScsAndLinks scsAndLinks = new ScsAndLinks();
		scsAndLinks.setScs(scs);
		scsAndLinks.setLinks(links);

		String scsAndLinksGson = gson.toJson(scsAndLinks);

		response.getWriter().write(scsAndLinksGson);

	}

	/**
	 * 灞曠ず鐗瑰緛瑙嗗浘
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void featureNodeGraph(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String[] instancedIds = { "#part", "#part1", "#part2", "#block1", "#block2", "#hole1", "#hole2", "#hole3",
				"#hole4", "#hole5", "#hole6", "#compoundfeature", "#hole7", "#hole8", "#hole9", "#hole10", "#hole11",
				"#hole12", "#hole13", "#hole14", "#hole15", "#concave1", "#concave2", "#concave3", "#696", "#697",
				"#698", "#699", "#700", "#701", "#702", "#703", "#704", "#706", "#707", "#708", "#709", "#710", "#713",
				"#714", "#715", "#716", "#717", "#718", "#719", "#720", "#721", "#705", "#711", "#712", "726", "727",
				"728", "729", "#730", "#731", "#733", "#734", "#722", "#723", "#724", "#738", "#740", "#744" };
		//,"#tolerance1" ,"#tolerance2"
		List<SemanticCell> scs = semanticAndLinkService.getSemanticCellsByIds(instancedIds);

		List<Link> links = semanticAndLinkService.getLinksByScs(scs);

		ScsAndLinks scsAndLinks = new ScsAndLinks();
		scsAndLinks.setScs(scs);
		scsAndLinks.setLinks(links);

		String scsAndLinksGson = gson.toJson(scsAndLinks);

		response.getWriter().write(scsAndLinksGson);

	}

	/**
	 * 灞曠ず璁捐鍘嗗彶
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getDesignHistory(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
//		System.out.println("hahahhahahahaaa拿到了"+designHistoryService.getFirstFace());
		// 绛涢�夊嚭鍥惧潡鏈�澶у疄浣撻潰涓惈鏈�2涓垨浠ヤ笂鐨勯潰锛岀粍鎴愭柊鐨刵ewBlockMap
		Map<String, List<String>> newBlockMap = new HashMap<String, List<String>>();
		newBlockMap = designHistoryService.maxBlockFace();
		// 鍒ゆ柇鍥惧潡涔嬮棿鐨勭埗瀛愮被鍏崇郴
		List<Block> blockList = new ArrayList<Block>();
		blockList = designHistoryService.baseFaces(newBlockMap);
		// 娣诲姞鍥惧潡鎵�鍖呭惈鐨勭壒寰�
		System.out.println("娣诲姞鍥惧潡鎵�鍖呭惈鐨勭壒寰�:-------------");
		List<Block> blockListFeatures = new ArrayList<Block>();
		blockListFeatures = designHistoryService.addBlockFeatures(blockList);
		for (int i = 0; i < blockListFeatures.size(); i++) {
			System.out.println(blockListFeatures.get(i));
		}
		// 娣诲姞鍥惧潡鐨勬渶澶у疄浣撻潰
		System.out.println("娣诲姞鍥惧潡鐨勬渶澶у疄浣撻潰:-------------");
		List<Block> blockListOk = new ArrayList<Block>();
		blockListOk = designHistoryService.addBlockFaces(blockListFeatures);
		for (int i = 0; i < blockListOk.size(); i++) {
			System.out.println(blockListOk.get(i));
		}
		// ---------浠ヤ笅浠ｇ爜涓昏涓烘壘鍑哄浘鍧椾腑鐨勬牴缁撶偣锛堝彲浠ュ皝瑁呮垚鏂规硶锛屾斁鍦╯ervice灞傦級-----------
		System.out.println("鏌ユ壘鍥惧潡鐨勬牴缁撶偣:-------------");
		List<String> allChildBlocks = new ArrayList<String>();
		for (int i = 0; i < blockListOk.size(); i++) {
			if (blockListOk.get(i).getBaseFaces() != null) {
				// 灏嗕竴涓浘鍧椾腑鎵�鏈夌殑瀛愬潡娣诲姞鍒颁竴涓狶ist涓�
				for (String baseFaces : blockListOk.get(i).getBaseFaces()) {
					List<String> childBlocks = new ArrayList<String>();
					childBlocks = blockListOk.get(i).getChildBlocks().get(baseFaces);
					allChildBlocks.addAll(childBlocks);
				}
			}
		}
		for (int i = 0; i < blockListOk.size(); i++) {
			if (allChildBlocks.contains(blockListOk.get(i).getBlockId())) {
				continue;
			} else {
				blockListOk.get(i).setIsRoot(1);// 鐢�1琛ㄧず鏍圭粨鐐癸紝鍏跺畠缁撶偣涓�-1.
				System.out.println(blockListOk.get(i).getBlockId() + "涓烘牴缁撶偣");
			}
		}
		// ---------浠ヤ笅浠ｇ爜鐢ㄤ簬鐢熸垚闆朵欢鐨勮璁″巻鍙�-------------
		System.out.println("鐢熸垚闆朵欢鐨勮璁″巻鍙�:-------------");
		DesignStep designStep = new DesignStep();
		// 璁ㄨ闆朵欢涓嶈兘鍒嗘垚瀛愬潡鐨勬儏鍐碉紝鍗充竴涓浂浠跺氨鏄竴涓潡鐨勬儏鍐�
		if (blockListOk.size() == 1) {
			System.out.println("璇ラ浂浠跺彧鏈変竴涓浘鍧�");
			// operations鐢ㄤ簬椤哄簭瀛樻斁姣忎釜姝ラ鐨勬搷浣�
			List<String> operations = new ArrayList<String>();
			// relationFaces鐢ㄤ簬椤哄簭瀛樻斁姣忔鎿嶄綔闇�瑕佸叧鑱旂殑闈�
			List<List<String>> relationFaces = new ArrayList<List<String>>();
			operations.add("鍑稿彴-鎷変几");
			relationFaces.add(blockListOk.get(0).getFaces());
			for (String feature : blockListOk.get(0).getFeatures()) {
				// 渚濇嵁鐗瑰緛绫诲瀷鍜岀壒寰佸寘鍚殑闈㈡坊鍔犲浘鍧楃殑鐗瑰緛璁捐鍘嗗彶銆傜敱浜庡浘鍧椾腑骞舵病鏈夊瓨鍏蜂綋鐨勭壒寰佺被鍨嬶紝鎵�鏈夌殑鐗瑰緛鐨勭被鍨嬮兘鏄痜eature锛�
				// 鍥犳闇�瑕佷娇鐢ㄦ鍒欒〃杈惧紡浠嶪D涓幏鍙栫壒寰佺殑鍏蜂綋绫诲瀷銆�
				Map<String, List<String>> featureRelation = new HashMap<String, List<String>>();
				featureRelation = designHistoryService.featureType(feature);
				for (String featureType : featureRelation.keySet()) {
					if (featureType.equals("hole")) {
						operations.add("瀛�-鍒囬櫎鎷変几");
						relationFaces.add(featureRelation.get(featureType));
					}
					if (featureType.equals("concave")) {
						operations.add("鐩存柟妲�-鍒囬櫎鎷変几");
						relationFaces.add(featureRelation.get(featureType));
					}
				}
			}
			// 閬嶅巻涓や釜List,灞曠ず璁捐鍘嗗彶
			designStep.setOperation(operations);
			designStep.setRelationFaces(relationFaces);
			for (int i = 0; i < designStep.getOperation().size(); i++) {
				System.out.println(designStep.getOperation().get(i));
			}
			for (int i = 0; i < designStep.getRelationFaces().size(); i++) {
				System.out.println(designStep.getRelationFaces().get(i));
			}
		}
		// 璁ㄨ闆朵欢鑳藉垎鎴愬涓潡鐨勬儏鍐�
		if (blockListOk.size() > 1) {
			// 璋冪敤鏂规硶锛屾墍鏈夊皢鍙剁粨鐐圭殑鍥惧潡涓惈鏈夌殑鐗瑰緛瀛樹竴涓猯eavesFeatureList
			List<String> allLeavesFeatures = new ArrayList<String>();
			allLeavesFeatures = designHistoryService.leavesFeature(blockListOk);
			// 濡傛灉涓�涓浂浠惰兘鍒嗘垚澶氫釜鍧楋紝鎴戜滑闇�瑕佹壘鍒版牴缁撶偣锛屼互鏍圭粨鐐逛负杩唬鐨勮捣鐐�
			for (int i = 0; i < blockListOk.size(); i++) {
				// 鏍圭粨鐐圭殑isRoot灞炴�т负1
				if (blockListOk.get(i).getIsRoot() == 1) {
					System.out.println(blockListOk.get(i).getBlockId());
					designStep = designHistoryService.designHistoryMap(blockListOk.get(i), allLeavesFeatures,
							blockListOk, designStep);
				}
			}
		}
		System.out.println("-----鏈�缁堟帹鐞嗗嚭鐨勮璁″巻鍙茬粨鏋滀负锛�-----");
		System.out.println(designStep);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(gson.toJson(designStep));
	}

	public void getFeatureNum(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String blockId = request.getParameter("blockId");
		response.setCharacterEncoding("UTF-8");
		HashMap<String, Integer> m = featureRelationService.getFeatureNumById(blockId);
		response.getWriter().write(gson.toJson(m));
	}
	
	
	public void getBlockFace(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		Map<String,String> blockFace = DesignHistoryService.blockFace();
		String blockId=request.getParameter("blockId");
		String faces = blockFace.get(blockId);
		String facesGson = gson.toJson(faces);
		response.getWriter().write(facesGson);
	}
	public void getFirstId(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		String firstFace=designHistoryService.getFirstFace();
		String firstFaceGson = gson.toJson(firstFace);
		response.getWriter().write(firstFaceGson);
//		System.out.println("hahahhahahahaaa拿到了"+designHistoryService.getFirstFace());
	}
}

