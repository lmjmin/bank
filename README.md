1. 구현된 항목
회원가입

회원가입 API는 구현되어 있다. AuthController의 /api/auth/signup에서 회원가입 요청을 받고, MemberService.regist()에서 회원 정보를 저장한 뒤 기본 계좌를 함께 생성한다. 또한 비밀번호는 BCryptPasswordEncoder로 암호화해서 저장하고 있다. MemberService에서는 회원 저장 후 계좌번호를 생성하고 잔액 0원, 상태 ACTIVE인 기본 계좌를 자동 생성한다.

로그인 및 JWT 발급

로그인 API도 구현되어 있다. /api/auth/login에서 사용자 조회 후 비밀번호를 검증하고, 일치하면 JWT 토큰을 발급해서 응답 본문과 Authorization 헤더에 넣어 준다. JWT 생성, 파싱, 유효성 검사는 JwtUtil에 구현되어 있다.

JWT 검증 필터

OncePerRequestFilter를 상속한 JwtAuthenticationFilter가 구현되어 있고, 요청 헤더의 Bearer 토큰을 읽어서 유효성을 검사한 뒤 SecurityContextHolder에 인증 정보를 넣는다. 이 점은 과제 지시사항의 “JWT 검증 필터 구현” 조건에 맞는다. 또한 SecurityConfig에서 해당 필터를 UsernamePasswordAuthenticationFilter 앞에 등록해 둔 상태다.

계층 분리

전체 구조는 Controller -> Service -> Repository 형태로 나뉘어 있다. 예를 들면 BankingController가 BankingService를 호출하고, BankingService가 AccountRepository, TransactionRepository, MemberRepository를 사용한다. AuthController도 MemberService를 통해 회원 관련 로직을 처리한다. 따라서 계층 분리 자체는 되어 있다고 볼 수 있다.

데이터 모델

과제에서 요구한 핵심 엔티티인 Member, Account, Transaction이 모두 존재한다. Member는 id, username, password, name, role을 가지고 있고, Account는 id, accountNumber, balance, member, status를 가진다. Transaction은 senderAccount, receiverAccount, amount, type, createdAt을 가진다. 요구사항과 비교하면 기본 뼈대는 갖춰져 있다.

입금 기능

입금 기능은 구현되어 있다. BankingController의 /api/banking/deposit가 BankingService.deposit()를 호출하고, 서비스에서는 계좌번호 조회, 금액 검증, 동결 계좌 검사 후 잔액을 증가시키고 거래 내역을 저장한다. TransactionType.DEPOSIT도 사용하고 있다.

송금 기능 및 트랜잭션 처리

송금 기능은 구현되어 있고, BankingService.transfer()에 @Transactional이 붙어 있다. 서비스에서는 송금 계좌/수취 계좌 조회, 금액 검증, 잔액 부족 검사, 동결 계좌 검사, 자기 자신 송금 방지까지 처리한다. 잔액 부족 시 RuntimeException("잔액 부족")을 발생시키므로 트랜잭션 롤백 조건도 충족한다. 이 부분은 과제 핵심 요구사항을 비교적 잘 맞춘 편이다.

내 계좌 조회 및 최근 거래 5건 조회

내 계좌 조회 API(/api/banking/my-account)와 최근 거래 조회 API(/api/banking/transactions)가 구현되어 있다. 최근 거래는 TransactionRepository.findTop5BySenderAccountOrReceiverAccountOrderByCreatedAtDesc()를 사용해서 최근 5건만 가져오도록 되어 있다.

2. 부분 구현된 항목
권한 제어 구조는 있으나 실제 적용은 안 됨

JWT 필터는 구현되어 있지만, 실제 URL 권한 제한은 적용되지 않았다. SecurityConfig에서 현재 requestMatchers("/**").permitAll()로 열어 둔 상태라서 모든 요청이 허용된다. 주석에는 관리자/사용자 권한 설정 코드가 남아 있지만 실제 동작 설정은 아니다. 따라서 과제의 /api/admin/** -> ADMIN, /api/banking/** -> USER 요구사항은 현재 구조만 있고 실제 적용은 안 된 상태다. 이 때문에 비로그인 사용자의 송금 API 호출 시 401, 일반 회원의 관리자 API 접근 시 403 같은 테스트는 통과하기 어렵다.

JWT를 쓰지만 컨트롤러에서 수동 처리

BankingController의 내 계좌 조회와 거래 조회는 직접 Authorization 헤더를 읽고 JWT를 파싱해서 username을 꺼내고 있다. JWT 필터가 이미 인증 정보를 SecurityContext에 넣어 주는데도 이를 활용하지 않고 수동으로 다시 처리하고 있다. 즉 동작은 일부 되지만, Spring Security 방식으로 깔끔하게 통합된 상태는 아니다.

DTO/Entity 분리는 되어 있지만 예외 처리는 미흡

DTO(MemberDto, DepositDto, TransferDto)와 Entity(Member, Account, Transaction)는 분리되어 있다. 하지만 예외 처리는 대부분 RuntimeException으로만 처리하고 있으며, @ControllerAdvice 같은 전역 예외 처리나 401/403/400에 맞는 응답 설계는 없다. 그래서 “적절한 예외 처리” 기준으로는 부분 구현 수준이다.

3. 아직 구현되지 않은 항목
관리자 기능

AdminController가 비어 있다. 따라서 과제에서 요구한 관리자 기능인 전체 회원 목록 조회, 특정 계좌 동결/해제, 전체 사용자 계좌 잔액 총합 조회는 아직 구현되지 않았다. 특히 필수 테스트 항목인 “ADMIN 계정으로 전체 사용자 계좌 잔액 총합 조회 가능 여부 확인”은 현재 코드상 수행할 수 없다.

관리자 API 권한 검증

관리자 컨트롤러가 비어 있을 뿐 아니라, 보안 설정 자체가 모든 경로 허용이라 일반 회원이 관리자 API 접근 시 403 Forbidden을 정확히 반환하는 조건도 충족하지 못한다.

비회원 공개 공지사항 조회

요구사항에는 비회원이 공개된 은행 공지사항을 조회할 수 있어야 한다고 되어 있는데, 업로드된 코드에는 공지사항 엔티티나 컨트롤러, 조회 API가 없다. 따라서 해당 기능은 미구현이다. 업로드된 파일 기준으로 확인되는 페이지 컨트롤러도 단순 화면 이동만 담당한다.

회원가입 시 역할 입력 처리

과제 명세에는 회원가입 시 ID, 비밀번호, 이름, 역할을 입력받는다고 되어 있지만, 실제 서비스에서는 memberDto의 role 값을 사용하지 않고 무조건 Role.USER로 저장한다. 즉 일반 사용자 가입은 가능하지만 “역할 입력 기반 가입”은 명세 그대로 구현된 것은 아니다.

4. 현재 코드의 중요한 문제점
입금이 “본인 계좌만” 되지 않음

요구사항에는 회원이 본인 계좌에 입금해야 한다고 되어 있는데, 현재 입금 API는 DepositDto로 전달받은 계좌번호만 기준으로 처리한다. 로그인한 사용자의 계좌인지 확인하는 로직이 없다. 즉 토큰과 상관없이 계좌번호만 알면 다른 사람 계좌에도 입금 가능하다. 기능은 있지만 요구사항과 완전히 일치하지는 않는다.

송금이 “내 계좌 기준”으로 보호되지 않음

송금도 TransferDto의 senderAccountNumber와 receiverAccountNumber를 그대로 받아서 처리한다. 현재 로그인한 사용자의 계좌가 sender 계좌인지 검증하지 않으므로, 계좌번호만 알면 다른 사람 계좌를 출금 계좌로 넣는 문제가 생길 수 있다. 과제의 “회원은 본인 계좌 기준으로 송금” 요구에 비해 보안상 부족하다.

필수 테스트 중 401/403 관련 항목 불통과 가능성이 큼

과제의 필수 테스트에는 “로그인하지 않은 상태에서 송금 API 호출 시 401”과 “일반 회원이 관리자 API 접근 시 403”이 있다. 하지만 현재 시큐리티 설정은 전체 경로를 permitAll()로 두고 있어서, 이 두 테스트는 명세대로 통과하지 못할 가능성이 높다. 송금 API 내부에서 별도 인증 체크도 없다.

최종 판정
된 것
회원가입
BCrypt 비밀번호 암호화
로그인 및 JWT 발급
JWT 검증 필터 구현
기본 계좌 자동 생성
입금
송금
@Transactional을 통한 송금 원자성 보장
내 계좌 조회
최근 거래 5건 조회
Controller / Service / Repository 계층 분리
DTO / Entity 분리
안 된 것
관리자 기능 전체
계좌 동결/해제 API
전체 회원 목록 조회 API
전체 계좌 잔액 총합 조회 API
공개 공지사항 조회 기능
실제 Security 권한 제한 적용
401 / 403 응답이 명세대로 나오게 하는 처리
회원가입 시 역할 입력 반영
부분 구현
JWT 기반 인증 구조
예외 처리
권한 제어 구조
본인 계좌 기준 입금/송금 검증
