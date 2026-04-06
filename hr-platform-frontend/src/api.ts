
import type { SkillDto } from './dto/SkillDto';
import type { CandidateDto } from './dto/CandidateDto';

const BASE_URL = 'http://localhost:8080/api';

export const api = {
  getAllCandidates: async (): Promise<CandidateDto[]> => {
    const res = await fetch(`${BASE_URL}/candidates`);
    if (!res.ok) throw new Error('Failed to fetch candidates');
    return res.json();
  },

  searchByName: async (name: string): Promise<CandidateDto[]> => {
    const res = await fetch(`${BASE_URL}/candidates/search-by-name?name=${encodeURIComponent(name)}`);
    if (!res.ok) throw new Error('Failed to search candidates by name');
    return res.json();
  },

  searchBySkill: async (skill: string): Promise<CandidateDto[]> => {
    const res = await fetch(`${BASE_URL}/candidates/search-by-skill?skill=${encodeURIComponent(skill)}`);
    if (!res.ok) throw new Error('Failed to search candidates by skill');
    return res.json();
  },

  addCandidate: async (candidate: Omit<CandidateDto, 'id' | 'skills'>): Promise<CandidateDto> => {
    const res = await fetch(`${BASE_URL}/candidates`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(candidate),
    });
    if (!res.ok) throw new Error('Failed to add candidate');
    return res.json();
  },

  removeCandidate: async (id: number): Promise<void> => {
    const res = await fetch(`${BASE_URL}/candidates/${id}`, {
      method: 'DELETE',
    });
    if (!res.ok) throw new Error('Failed to remove candidate');
  },

  addSkill: async (candidateId: number, skillData: { skillName: string }): Promise<SkillDto> => {
    const res = await fetch(`${BASE_URL}/candidates/${candidateId}/skills`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(skillData),
    });
    if (!res.ok) throw new Error('Failed to add skill');
    return res.json();
  },

  removeSkill: async (candidateId: number, skillId: number): Promise<void> => {
    const res = await fetch(`${BASE_URL}/candidates/${candidateId}/skills/${skillId}`, {
      method: 'DELETE',
    });
    if (!res.ok) throw new Error('Failed to remove skill');
  }
};
