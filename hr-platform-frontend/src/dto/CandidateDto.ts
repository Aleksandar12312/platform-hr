import type { SkillDto } from './SkillDto';

export interface CandidateDto {
  id: number;
  name: string;
  email: string;
  contactNumber: string;
  birthday?: string;
  skills?: SkillDto[];
}
