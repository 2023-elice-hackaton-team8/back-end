from dotenv import load_dotenv
from PyPDF2 import PdfReader
from langchain.text_splitter import CharacterTextSplitter
from langchain.embeddings import OpenAIEmbeddings, HuggingFaceInstructEmbeddings
from langchain.vectorstores import FAISS
from langchain.chat_models import ChatOpenAI
from langchain.memory import ConversationBufferMemory
from langchain.chains import ConversationalRetrievalChain
from langchain.llms import HuggingFaceHub
from langchain.document_loaders import PyPDFium2Loader
from langchain.prompts import PromptTemplate
from langchain.chains.llm import LLMChain
from langchain.chains.summarize import load_summarize_chain
from langchain.chains.combine_documents.stuff import StuffDocumentsChain
from langchain.schema import Document
from tqdm import tqdm
from langchain.retrievers.multi_query import MultiQueryRetriever
import os
import openai
from langchain.prompts import ChatPromptTemplate


class Feedback:
   
    def __init__(self):
        # API Key값 $OPEN_API_KEY
        self.llm = ChatOpenAI(model_name="gpt-3.5-turbo")

        # PDF URL
        # raw_text = get_pdf_text(["~/social_removed.pdf"])
        self.raw_text = self.get_pdf_text(["social_removed.pdf"])

        self.text_chunks = self.get_text_chunks(self.raw_text)
        self.vectorstore = self.get_vectorstore(self.text_chunks)

    def get_pdf_text(self, pdf_docs):
        text = ""
        for pdf in pdf_docs:
            loader = PyPDFium2Loader(pdf)
            pdf_reader = loader.load()
            for page in pdf_reader:
                text += page.page_content
        return text

    def get_text_chunks(self, text):
        text_splitter = CharacterTextSplitter(
            separator="\n",
            chunk_size=1024,
            chunk_overlap=128,
            length_function=len
        )
        chunks = text_splitter.split_text(text)
        return chunks


    def get_vectorstore(self, text_chunks):
        embeddings = OpenAIEmbeddings()
        vectorstore = FAISS.from_texts(texts=text_chunks, embedding=embeddings)
        return vectorstore


    def return_feedback(self, prob_type:bool, QUESTION:str, ANSWER:str, WRONGANSWER:str):
        # 받아야 할 파라미터
        # prob_type: 문제 유형에 대한 정보 (True: 고급, False: 기본)
        # QUESTION: 기본 유형에 해당하는 문장
        # ANSWER: 실제 정답 (단어 혹은 문장)
        # WRONGANSWER: 사용자가 오답으로 적은 문장

        ## 고급 유형 예제
        # ANSWER = "해당 섹션은 '사회 계층과 불평등/사회 불평등 현상의 이해'에 관한 내용을 다루고 있습니다. 이 내용을 요약하면 다음과 같습니다.\n\n사회 불평등 현상은 경제적, 정치적, 사회·문화적 측면으로 나타납니다. 경제적 불평등은 소득이나 재산의 차등 분배로 인해 발생하며 가장 일반적인 형태입니다. 정치적 불평등은 권력의 소유나 정치 참여 기회에 따른 차등이며, 특정 계층이나 집단의 정치 참여를 배제하면 심화될 수 있습니다. 사회·문화적 불평등은 사회적 위신, 명예, 건강, 문화 등이 차등 분배되어 발생합니다. 이러한 불평등 현상은 사회 구성원들 간의 생활 양식, 가치관, 사고방식의 차이를 초래하며, 경쟁을 유발하여 사회적 효율성을 높이기도 하지만 갈등을 유발하여 사회 통합을 저해할 수도 있습니다.\n\n사회 불평등 현상은 모든 사회에서 보편적으로 존재하며, 시대와 사회에 따라 사회적 희소가치에 의해 나타납니다. 한 시대에는 위신이나 명예가 사회 불평등 현상에 영향을 미치는 반면, 다른 시대에는 학력이나 직업, 소득이나 재산이 중요한 희소가치입니다. 이러한 현상은 시대와 사회에 따라 특수성을 갖지만 사회 계층화 현상은 보편적으로 존재합니다.\n\n사회 불평등 현상은 사회 구성원들 간의 생활 방식, 가치관, 사고방식의 차이를 초래하며, 경쟁을 유발하여 사회적 효율성을 높이기도 하지만 갈등을 유발하여 사회 통합을 저해하기도 합니다. 이를 통해 사회 불평등은 모든 사회와 모든 시대에 존재하는 보편적인 현상이며, 완전히 평등한 사회는 거의 존재하지 않습니다.\n해당 섹션에서 다루는 주제는 '사회 계층과 불평등/사회 불평등 현상의 이해'입니다. 이 섹션에서는 사회 계층과 관련된 여러 용어와 개념을 설명하고 대표적인 이론인 구분 계급론(마르크스)과 계층론(베버)에 대해 소개합니다.\n\n먼저, 사회 계층화 현상에 대해 설명합니다. 이 현상은 산업 사회에서 나타나는 현상으로 카스트 제도와 범주화를 통해 설명됩니다. 카스트 제도는 서로 다른 신분 간 혼인을 금지하고 신분의 철저한 세습으로 폐쇄성이 강한 제도입니다. 범주화는 여러 사물이나 현상, 개념을 공통적인 속성을 가진 것들끼리 묶어 분류하는 과정입니다.\n\n그 후에는 사회 계층화 현상에 관한 대표적인 이론인 구분 계급론(마르크스)과 계층론(베버)을 설명합니다. 구분 계급론은 계급을 생산 수단을 둘러싸고 나타나는 위계 구조에서 공통의 위치를 차지하는 사람들의 집합체로 정의하며, 생산 수단의 소유 여부에 따라 계급을 구분합니다. 계층론은 다양한 요인에 의해 범주화된 각각의 위계 서열에서 공통의 위치를 차지하는 사람들의 집합체로 정의하며, 다양한 요인에 의해 사회 불평등이 발생함을 강조합니다.\n\n또한 사회 불평등 현상을 바라보는 관점으로 기능론과 갈등론을 제시합니다. 기능론은 차등 분배 체계가 사회 구성원의 성취동기를 부여하고 구성원 간 경쟁을 유발하여 사회 발전에 기여한다고 보는 관점입니다. 반면 갈등론은 사회 불평등 현상이 지배 집단에게 유리한 분배 제도로 인해 나타난다고 보는 관점입니다. \n\n이 섹션을 통해 '사회 계층과 불평등/사회 불평등 현상의 이해'에 대한 내용을 이해할 수 있습니다.\n해당 섹션은 사회 계층과 불평등 현상에 대해 다이아몬드형 계층 구조와 계층 간의 상승 및 하강 이동을 파악하는 방법을 설명하고 있습니다. 세대 내 이동과 세대 간 이동을 비교하기 위해 개인의 계층은 주로 직업을 기준으로 삼습니다. 이동을 파악하는 방법은 개인의 최초 계층과 현재 계층을 비교하는 것입니다. 세대 간 이동을 조사한 결과, 상승 이동자가 하강 이동자보다 더 많은 것으로 나타났습니다. 세대 간 이동을 파악하기 위해 다이아몬드형 계층 구조를 활용하는 것이 부모 세대의 계층 구조보다 사회 통합과 안정을 실현하는 데 유리합니다. 이러한 분석을 통해 사회 계층과 불평등 현상을 이해할 수 있습니다.\n해당 섹션은 사회 계층과 불평등에 대한 기능론과 갈등론 관점을 설명하고 있다. 기능론은 사회적 희소가치의 차등 분배가 사회의 효율성과 발전에 기여한다고 주장하며, 갈등론은 사회적 희소가치의 차등 분배가 사회적 갈등과 대립 관계를 형성한다고 보고 있다. 이 구절은 이러한 두 관점을 설명하면서 불평등 현상은 시대와 사회를 초월한 보편적인 현상이며 사회 구조를 변혁하여 불평등을 타파해야 한다고 언급하고 있다. 이러한 관점을 균형 있게 이해함으로써 사회 불평등의 순기능을 살리고 동시에 차등 분배로 인해 나타나는 문제점을 개선할 필요가 있다고 말하고 있다. 마지막으로 사회적 계층화는 경제적, 사회적, 정치적 차원에서 나타남을 강조하고 있다."
        # WRONGANSWER = '사회 계층과 불평등 현상에 대한 이해를 다루고 있습니다. 사회 불평등은 경제, 정치, 사회·문화적인 측면에서 나타납니다. 사회 계층화는 구성원들 간의 불평등을 일정한 기준으로 나누고 서열화하는 현상으로, 시대에 따라 변화합니다. 이 섹션에서는 사회 계층화와 불평등 현상을 다양한 관점에서 설명하며, 기능론과 갈등론의 관점도 소개합니다. 기능론은 불평등이 효율성에 기여하며 불가피하다고 주장하고, 갈등론은 불평등이 갈등을 유발하며 사회 구조 변혁이 필요하다고 주장합니다.'

        if prob_type:
            
            template="""
            'ANSWER'는 실제 정답이고, 'WRONGANSWER'는 사용자가 작성한 문장이다.
            ANSWER={ANSWER}
            WRONGANSWER={WRONGANSWER}
            ANSWER 와 WRONGANSWER를 비교하여 부족한 부분에 대해 생성하시오.
            ('ANSWER' 와 'WRONGANSWER'라는 단어는 반환하면 안된다.)

            """
            prompt_template = ChatPromptTemplate.from_template(template)

            prompt_template = prompt_template.format_messages(
                                ANSWER=ANSWER,
                                WRONGANSWER=WRONGANSWER)  
            feedback = self.llm(prompt_template).content.replace('\n', ' ')
            
            return feedback
        

        ## 기본 유형 예제
        # QUESTION="'문화와 일상생활/현대 사회의 문화 양상'은 사회 문화 현상을 탐구하고 이해하는 것을 강조하며, 양적 연구와 질적 연구를 사용하여 사회 _ _ _ _을 깊이 이해하는 방법을 설명합니다."
        # ANSWER="문화 현상"
        # WRONGANSWER="문화 오버"

        else:
            ## 문서 검색
            retriever = self.vectorstore.as_retriever(search_type="similarity_score_threshold", search_kwargs={"score_threshold": .5}, k=6)

            ## 전체 Doc에서 LESSON과 관련있는 문서 검색해서 가져오기
            query="""
            [ANSWER]이/가 적힌 첫 부분 모두 반환하시오.
            """.replace("[ANSWER]", ANSWER)

            docs = retriever.get_relevant_documents(query)

            # Doc화
            summary_list = [doc.page_content for doc in docs]
            summaries = "\n".join(summary_list)
            summaries = Document(page_content=summaries)

            map_prompt = """
            아래 빈칸 맞추기 문제에 단어로 적절한 단어는 '[ANSWER]'이다.
            "[QUESTION]"
            아래에 '[ANSWER]'에 관련된 한 구절이 주어집니다. 해당 섹션은 세 개의 백틱(```)으로 감싸져 있습니다.

            ```{text}```

            '[ANSWER]'에 대해 사용자가 완벽하게 이해할 수 있도록 '[ANSWER]'의 정확한 개념과 실제로 사용되는 개념 예시를 찾아서 생성하시오.
            """.replace("[ANSWER]", ANSWER).replace("[QUESTION]", QUESTION)

            map_prompt_template = PromptTemplate(template=map_prompt, input_variables=["text"])

            reduce_chain = load_summarize_chain(llm=self.llm,
                                        chain_type="stuff",
                                        prompt=map_prompt_template)

            feedback = reduce_chain.run([summaries]).replace('\n', ' ')
            
            return feedback


#####
## 고급 example 
F = Feedback()
example_1 = F.return_feedback(
              prob_type=True,
              QUESTION='',
              ANSWER = "해당 섹션은 '사회 계층과 불평등/사회 불평등 현상의 이해'에 관한 내용을 다루고 있습니다. 이 내용을 요약하면 다음과 같습니다.\n\n사회 불평등 현상은 경제적, 정치적, 사회·문화적 측면으로 나타납니다. 경제적 불평등은 소득이나 재산의 차등 분배로 인해 발생하며 가장 일반적인 형태입니다. 정치적 불평등은 권력의 소유나 정치 참여 기회에 따른 차등이며, 특정 계층이나 집단의 정치 참여를 배제하면 심화될 수 있습니다. 사회·문화적 불평등은 사회적 위신, 명예, 건강, 문화 등이 차등 분배되어 발생합니다. 이러한 불평등 현상은 사회 구성원들 간의 생활 양식, 가치관, 사고방식의 차이를 초래하며, 경쟁을 유발하여 사회적 효율성을 높이기도 하지만 갈등을 유발하여 사회 통합을 저해할 수도 있습니다.\n\n사회 불평등 현상은 모든 사회에서 보편적으로 존재하며, 시대와 사회에 따라 사회적 희소가치에 의해 나타납니다. 한 시대에는 위신이나 명예가 사회 불평등 현상에 영향을 미치는 반면, 다른 시대에는 학력이나 직업, 소득이나 재산이 중요한 희소가치입니다. 이러한 현상은 시대와 사회에 따라 특수성을 갖지만 사회 계층화 현상은 보편적으로 존재합니다.\n\n사회 불평등 현상은 사회 구성원들 간의 생활 방식, 가치관, 사고방식의 차이를 초래하며, 경쟁을 유발하여 사회적 효율성을 높이기도 하지만 갈등을 유발하여 사회 통합을 저해하기도 합니다. 이를 통해 사회 불평등은 모든 사회와 모든 시대에 존재하는 보편적인 현상이며, 완전히 평등한 사회는 거의 존재하지 않습니다.\n해당 섹션에서 다루는 주제는 '사회 계층과 불평등/사회 불평등 현상의 이해'입니다. 이 섹션에서는 사회 계층과 관련된 여러 용어와 개념을 설명하고 대표적인 이론인 구분 계급론(마르크스)과 계층론(베버)에 대해 소개합니다.\n\n먼저, 사회 계층화 현상에 대해 설명합니다. 이 현상은 산업 사회에서 나타나는 현상으로 카스트 제도와 범주화를 통해 설명됩니다. 카스트 제도는 서로 다른 신분 간 혼인을 금지하고 신분의 철저한 세습으로 폐쇄성이 강한 제도입니다. 범주화는 여러 사물이나 현상, 개념을 공통적인 속성을 가진 것들끼리 묶어 분류하는 과정입니다.\n\n그 후에는 사회 계층화 현상에 관한 대표적인 이론인 구분 계급론(마르크스)과 계층론(베버)을 설명합니다. 구분 계급론은 계급을 생산 수단을 둘러싸고 나타나는 위계 구조에서 공통의 위치를 차지하는 사람들의 집합체로 정의하며, 생산 수단의 소유 여부에 따라 계급을 구분합니다. 계층론은 다양한 요인에 의해 범주화된 각각의 위계 서열에서 공통의 위치를 차지하는 사람들의 집합체로 정의하며, 다양한 요인에 의해 사회 불평등이 발생함을 강조합니다.\n\n또한 사회 불평등 현상을 바라보는 관점으로 기능론과 갈등론을 제시합니다. 기능론은 차등 분배 체계가 사회 구성원의 성취동기를 부여하고 구성원 간 경쟁을 유발하여 사회 발전에 기여한다고 보는 관점입니다. 반면 갈등론은 사회 불평등 현상이 지배 집단에게 유리한 분배 제도로 인해 나타난다고 보는 관점입니다. \n\n이 섹션을 통해 '사회 계층과 불평등/사회 불평등 현상의 이해'에 대한 내용을 이해할 수 있습니다.\n해당 섹션은 사회 계층과 불평등 현상에 대해 다이아몬드형 계층 구조와 계층 간의 상승 및 하강 이동을 파악하는 방법을 설명하고 있습니다. 세대 내 이동과 세대 간 이동을 비교하기 위해 개인의 계층은 주로 직업을 기준으로 삼습니다. 이동을 파악하는 방법은 개인의 최초 계층과 현재 계층을 비교하는 것입니다. 세대 간 이동을 조사한 결과, 상승 이동자가 하강 이동자보다 더 많은 것으로 나타났습니다. 세대 간 이동을 파악하기 위해 다이아몬드형 계층 구조를 활용하는 것이 부모 세대의 계층 구조보다 사회 통합과 안정을 실현하는 데 유리합니다. 이러한 분석을 통해 사회 계층과 불평등 현상을 이해할 수 있습니다.\n해당 섹션은 사회 계층과 불평등에 대한 기능론과 갈등론 관점을 설명하고 있다. 기능론은 사회적 희소가치의 차등 분배가 사회의 효율성과 발전에 기여한다고 주장하며, 갈등론은 사회적 희소가치의 차등 분배가 사회적 갈등과 대립 관계를 형성한다고 보고 있다. 이 구절은 이러한 두 관점을 설명하면서 불평등 현상은 시대와 사회를 초월한 보편적인 현상이며 사회 구조를 변혁하여 불평등을 타파해야 한다고 언급하고 있다. 이러한 관점을 균형 있게 이해함으로써 사회 불평등의 순기능을 살리고 동시에 차등 분배로 인해 나타나는 문제점을 개선할 필요가 있다고 말하고 있다. 마지막으로 사회적 계층화는 경제적, 사회적, 정치적 차원에서 나타남을 강조하고 있다.",
              WRONGANSWER = '사회 계층과 불평등 현상에 대한 이해를 다루고 있습니다. 사회 불평등은 경제, 정치, 사회·문화적인 측면에서 나타납니다. 사회 계층화는 구성원들 간의 불평등을 일정한 기준으로 나누고 서열화하는 현상으로, 시대에 따라 변화합니다. 이 섹션에서는 사회 계층화와 불평등 현상을 다양한 관점에서 설명하며, 기능론과 갈등론의 관점도 소개합니다. 기능론은 불평등이 효율성에 기여하며 불가피하다고 주장하고, 갈등론은 불평등이 갈등을 유발하며 사회 구조 변혁이 필요하다고 주장합니다.'
          )
## 기본 example
example_2 = F.return_feedback(
              prob_type=False,
              QUESTION="'문화와 일상생활/현대 사회의 문화 양상'은 사회 문화 현상을 탐구하고 이해하는 것을 강조하며, 양적 연구와 질적 연구를 사용하여 사회 _ _ _ _을 깊이 이해하는 방법을 설명합니다.",
              ANSWER="문화 현상",
              WRONGANSWER="문화 오버"
            )

print(example_1)

print(example_2)
