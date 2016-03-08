package FileSender.common;

public final class BaseCode {
	public static final String CR = "\r";
	public static final String LF = "\n";
	public static final String CRLF = "\r\n";

	public static final String USER_SESSIONKEY = "loginUser";

	public static final String YES	= "Y";
	public static final String NO	= "N";

	public static final int LOGIN_RESTRICT_DISABLE 	= 0;	// 로그인 접속제한 사용 안함
	public static final int LOGIN_RESTRICT_BY_IP 	= 1; 	// 로그인 IP로 접속제한

	public static final int AUTH_TYPE_ADMIN 		= 1; 	// 관리자
	public static final int AUTH_TYPE_MANAGER 		= 2; 	// 중간 관리자
	public static final int AUTH_TYPE_STANDARD 		= 3; 	// 일반 사용자
	public static final int AUTH_TYPE_ANONYMOUS  	= 4; 	// 비인가 사용자 

	public static final int SYNC_CACHE 					= 1; 	// 캐쉬 동기화
	public static final int SYNC_FILE_POLICY			= 2; 	// 파일 정책 동기화
	public static final int SYNC_LOGIN_POLICY			= 3; 	// 로그인 정책 동기화
	public static final int SYNC_EMAIL_POLICY		 	= 4; 	// 파일 정책 동기화
	public static final int SYNC_MAKE_USER_AGENT_DIR 	= 5; 	// 사용자 추가시 에이전트 폴더 동기화
	public static final int SYNC_MAKE_CLIPBOARD_FILE 	= 6; 	// 에이전트 클립보드 정책 파일 생성

	public static final String SYNC_UNDEFINED_CODE 		= "U"; 	// 정의 되지 않은 SYNC 코드 값

	public static final String DUAL_SYSTEM_COPY_NONE		= "N"; // 이중화 사용 안하는 경우
	public static final String DUAL_SYSTEM_COPY_WAIT		= "W"; // 이중화 사용 파일 복사 중
	public static final String DUAL_SYSTEM_COPY_COMPLETE	= "C"; // 이중화 사용 파일 복사 완료

	public static final String FILE_SYNC_SENDER_TYPE_TX	= "T"; // 파일 전송 측에서 복사
	public static final String FILE_SYNC_SENDER_TYPE_RX	= "R"; // 파일 받는 측에서 복사	

	public static final String APPROVAL_WAIT_ALL			= "All"; // 일반승인대기, 사후승인대기, 부재중 승인대기 -> 대기중인 승인 전체
	public static final String APPROVAL_TURN				= "A"; // 승인할 차례
	public static final String APPROVAL_WAIT				= "W"; // 선순위 승인자의 승인대기
	public static final String APPROVAL_REJECT				= "R"; // 반려
	public static final String APPROVAL_EXPIRE				= "E"; // 결재기간 만료
	public static final String APPROVAL_PROXY_REJECT		= "PR"; // 대리반려
	public static final String APPROVAL_PASS				= "P"; // 선순위 승인자의 반려로 인한 스킵.
	public static final String APPROVAL_ALLOW				= "Y"; // 승인.
	public static final String APPROVAL_PROXY_ALLOW			= "PY"; // 대리승인.
	public static final String APPROVAL_DELETE				= "D"; // 삭제.
	public static final String APPROVAL_BACK				= "B"; // 사후승인 
	public static final String APPROVAL_BACK_NO_CONFIRM		= "BN"; // 사후승인 미확인 
	public static final String APPROVAL_BACK_CONFIRM		= "BC"; // 확인 된 사후승인 
	public static final String APPROVAL_NONE				= "N"; // 승인없음 

	public static final String APPROVAL_POLICY_NORMAL		= "normal"; // 일반승인 
	public static final String APPROVAL_POLICY_SELF			= "self"; // 자가승인 
	public static final String APPROVAL_POLICY_AFTER		= "after"; // 사후승인 
	public static final String APPROVAL_POLICY_ABSENT		= "absent"; // 부재중승인 
	public static final String APPROVAL_POLICY_PROXY		= "proxy"; // 대리승인 
	public static final String APPROVAL_POLICY_AFTER_PROXY	= "afterproxy"; // 사후 + 대리승인 

	public static final String APPROVAL_USE_TYPE_ALWAYS 	= "A"; // 항상
	public static final String APPROVAL_USE_TYPE_NONE 		= "N"; // 사용안함
	public static final String APPROVAL_USE_TYPE_SCHEDULE 	= "S"; // 스케줄

	public static final String SEND_TYPE_FILE_IN2OUT	= "In"; // 내부망에서 전송
	public static final String SEND_TYPE_FILE_OUT2IN	= "Out"; // 외부망에서 전송
	public static final String SEND_TYPE_EMAIL			= "email"; // 메일전송

	public static final String SEND_STATUS_WAIT		= "N"; // 전송대기
	public static final String SEND_STATUS_FAIL		= "F"; // 전송실패
	public static final String SEND_STATUS_VC		= "V"; // AV 검사중
	public static final String SEND_STATUS_APPROVAL_WAIT	= "A"; // 승인대기
	public static final String SEND_STATUS_REJECT	= "R"; // 반려
	public static final String SEND_STATUS_MALWARE	= "M"; // 유해
	public static final String SEND_STATUS_COMPLETE	= "Y"; // 전송완료

	public static final String LOGGING_STATUS_NO		= "N"; // 원본로깅 안함
	public static final String LOGGING_STATUS_YES		= "Y"; // 원본로깅 완료
	public static final String LOGGING_STATUS_EXPECT	= "E"; // 원본로깅 예정

	public static final String SEND_FROM_IN_TO_OUT	= "In";		// 내부망에서 외부망으로
	public static final String SEND_FROM_OUT_TO_IN	= "Out";	// 외부망에서 내부망으로
	public static final String SEND_FROM_IN_TO_IN	= "InToIn"; // 내부망끼리 전송.

	public static final String SLAVE_FILE_STATUS_NONE	= "N"; // 파일 원래 없음
	public static final String SLAVE_FILE_STATUS_EXIST	= "E"; // 파일 존재
	public static final String SLAVE_FILE_STATUS_DELETE	= "D"; // 파일 삭제

	public static final String PROXY_STATUS_DELETE		= "D"; // 대리승인자 삭제
	public static final String PROXY_STATUS_MOD			= "M"; // 대리승인자 변경
	public static final String PROXY_STATUS_INSERT		= "I"; // 대리승인자 추가

	public static final String NETWORK_POSITON_IN			= "In";			// 내부망에서 접속.
	public static final String NETWORK_POSITON_OUT			= "Out";		// 외부망에서 접속.
	public static final String NETWORK_POSITON_SLAVE_IN		= "slaveIn";	// slave내부망에서 접속.
	public static final String NETWORK_POSITON_SLAVE_OUT	= "slaveOut";	// slave외부망에서 접속.

	public static final String NO_APPROVER			= "0";		// 승인권 없음.
	public static final String FIRST_APPROVER		= "1";		// 1차 승인권자
	public static final String SECOND_APPROVER		= "2";		// 2차 승인권자

	public static final String SELF_APPROVAL_ON		= "on";		// 전결
	public static final String SELF_APPROVAL_OFF	= "off";	// 전결 아님.

	public static final String AFTER_APPROVAL_ON	= "on";		// 사후승인
	public static final String AFTER_APPROVAL_OFF	= "off";	// 사후승인 아님.

	public static final int FILE_SEND_STATUS_WAIT		= 0;		// 첨부파일 전송 대기
	public static final int FILE_SEND_STATUS_FAIL		= 1;		// 첨부파일 전송 실패
	public static final int FILE_SEND_STATUS_COMPLETE	= 2;		// 첨부파일 전송 완료

	public static final int DEFAULT_SESSION_TIMEOUT = 30;

	public static final String ZIP_FILE_EXTENSION = "zip";
	public static final String ZIP_FILE_FULL_EXTENSION = "." + ZIP_FILE_EXTENSION;

	public static final String EML_FILE_EXTENSION = "eml";
	public static final String EML_FILE_FULL_EXTENSION = "." + EML_FILE_EXTENSION;

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static final String RSA_KEY_PAIR_SESSION = "__rsaKeyPair__";
}